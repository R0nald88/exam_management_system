package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class QuestionDatabase extends Database<Question> {
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
        QuestionType t = type == null || type.isEmpty() ? null : QuestionType.valueOf(type);

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

    public void addQuestion(Question question) throws Exception{
        if (exist(question) && question.equals(queryByKey(question.getId() + ""))) {
            throw new Exception("Question duplicated.");
        } else if (exist(question)) {
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

    public void deleteQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }

        if (!exist(question)) {
            throw new Exception("Question \"" + question.getQuestion() + "\" does not exist.");
        }

        delByKey(question.getId().toString());
    }

    public void deleteQuestions(List<Question> questions) throws Exception {
        for (Question q : questions) {
            deleteQuestion(q);
        }
    }
}
