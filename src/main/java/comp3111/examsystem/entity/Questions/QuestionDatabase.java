package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.tools.Database;

import java.util.List;

public class QuestionDatabase extends Database<Question> {
    public static final int OPTION_LENGTH_LIMIT = 20;
    public static final int ANSWER_LENGTH_LIMIT = -1;
    public static final int QUESTION_LENGTH_LIMIT = -1;
    public static final int SCORE_UPPER_LIMIT = -1;
    public static final int SCORE_LOWER_LIMIT = 0;

    private static QuestionDatabase instance = null;

    private QuestionDatabase() {
        super(Question.class);
    }

    public static QuestionDatabase getInstance() {
        if (instance == null) instance = new QuestionDatabase();
        return instance;
    }

    public List<Question> filter(String question, QuestionType type, int score) {
        List<Question> questionList =
                (question == null || question.isEmpty()) ?
                getAll() : queryFuzzyByField("question", question);

        List<Question> typeList =
                type == null ? getAll() : queryByField("type", type.toString());

        List<Question> scoreList =
                score <= 0 ? getAll() : queryByField("score", score + "");

        return join(questionList, join(typeList, scoreList));
    }

    public List<Question> filter(String question, String type, String score) throws Exception {
        int s = -1;
        QuestionType t = type == null || type.isEmpty() ? null : QuestionType.toType(type);

        if (score != null && !score.isEmpty()) {
            try {
                s = Integer.parseInt(score);
            } catch (Exception e) {
                throw new Exception("Score should be an integer.");
            }
        }

        return filter(question, t, s);
    }

    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    public boolean exist(Question question) {
        return exist(question.getId());
    }

    public void addQuestion(Question question) throws Exception {
        List<Question> q = filter(question.getQuestion(), question.getType(), question.getScore());

        for (Question i : q) {
            if (i.equals(question)) {
                throw new Exception("Question duplicated.");
            }
        }

        if (exist(question)) {
            question.setId(System.currentTimeMillis());
        }

        add(question);
    }

    public void updateQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }

        if (!exist(question)) {
            throw new Exception("Question \"" + question.getQuestion() + "\" does not exist.");
        }

        update(question);
    }

    public void deleteQuestion(Question question, boolean deleteExam) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }

        if (!exist(question)) {
            throw new Exception("Question \"" + question.getQuestion() + "\" does not exist.");
        }

        List<Exam> affectedExam = ExamDatabase.getInstance().queryByQuestion(question.getId());

        if (!deleteExam && !affectedExam.isEmpty()) {
            throw new RuntimeException("Some exam contains the selected question(s). Deleting which may affect the exam(s).");
        } else if (!affectedExam.isEmpty()) {
            affectedExam.removeIf(e -> e.getQuestionIds().size() == 1 && e.getQuestionIds().contains(question.getId()));
            ExamDatabase.getInstance().deleteExams(affectedExam);
        }

        delByKey(question.getId().toString());
    }

    public void deleteQuestions(List<Question> questions, boolean deleteExam) throws Exception {
        for (Question q : questions) {
            deleteQuestion(q, deleteExam);
        }
    }

    public void deleteAll() throws Exception {
        deleteQuestions(getAll(), true);
    }
}
