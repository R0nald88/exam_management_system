package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.tools.Database;

import java.util.List;

/**
 * Database for accessing and storing question entity
 * @author Cheung Tuen King
 */
public class QuestionDatabase extends Database<Question> {
    /**
     * Length limit of options. Set to -1 for removing length limit
     */
    public static final int OPTION_LENGTH_LIMIT = 20;

    /**
     * Length limit of answer. Set to -1 for removing length limit
     */
    public static final int ANSWER_LENGTH_LIMIT = -1;

    /**
     * Length limit of question. Set to -1 for removing length limit
     */
    public static final int QUESTION_LENGTH_LIMIT = -1;

    /**
     * Upper limit of score. Set to -1 for removing upper limit
     */
    public static final int SCORE_UPPER_LIMIT = -1;

    /**
     * Lower limit of options
     */
    public static final int SCORE_LOWER_LIMIT = 0;

    /**
     * QuestionDatabase singleton instance
     */
    private static QuestionDatabase instance = null;

    /**
     * Constructor of QuestionDatabase
     * @author Cheung Tuen King
     */
    private QuestionDatabase() {
        super(Question.class);
    }

    /**
     * Access QuestionDatabase singleton
     * @author Cheung Tuen King
     * @return QuestionDatabase singleton instance
     */
    public static QuestionDatabase getInstance() {
        if (instance == null) instance = new QuestionDatabase();
        return instance;
    }

    /**
     * Filter the question in the database based on parameters provided.
     *
     * @param question Question text for filtering.
     *                 Perform fuzzy matching on this field.
     *                 Do not perform filtering if this field is empty or null.
     *
     * @param type Question type for filtering.
     *             Perform exact matching on this field.
     *             Do not perform filtering if this field is null.
     *
     * @param score Score for filtering.
     *              Perform exact matching on this field.
     *              Do not perform filtering if this field is non-positive integer.
     *
     * @return List of questions matching the parameters
     * @author Cheung Tuen King
     */
    public List<Question> filter(String question, QuestionType type, int score) {
        List<Question> questionList =
                (question == null || question.trim().isEmpty()) ?
                getAll() : queryFuzzyByField("question", question.trim());

        List<Question> typeList =
                type == null ? getAll() : queryByField("type", type.toString());

        List<Question> scoreList =
                score <= 0 ? getAll() : queryByField("score", score + "");

        return join(questionList, join(typeList, scoreList));
    }

    /**
     * Filter the question in the database based on parameters provided.
     *
     * @param question Question text for filtering.
     *                 Perform fuzzy matching on this field.
     *                 Do not perform filtering if this field is empty or null.
     *
     * @param type Question type for filtering.
     *             Perform exact matching on this field.
     *             Do not perform filtering if this field is empty or null.
     *
     * @param score Score for filtering.
     *              Perform exact matching on this field.
     *              Do not perform filtering if this field is non-positive, non-integer, empty or null.
     *
     * @return List of questions matching the parameters
     * @author Cheung Tuen King
     * @throws Exception Score is not an integer
     */
    public List<Question> filter(String question, String type, String score) throws Exception {
        int s = -1;
        QuestionType t = type == null || type.isEmpty() ? null : QuestionType.toType(type);

        if (score != null && !score.trim().isEmpty()) {
            try {
                s = Integer.parseInt(score.trim());
            } catch (Exception e) {
                throw new Exception("Score should be an integer.");
            }
        }

        return filter(question, t, s);
    }

    /**
     * Check if the provided question ID exists
     *
     * @param id Question id for checking
     * @return Boolean determining if the question ID provided exists
     * @author Cheung Tuen King
     */
    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    /**
     * Check if the provided question exists by checking its ID
     *
     * @param question Question for checking
     * @return Boolean determining if the question provided exists
     * @author Cheung Tuen King
     */
    public boolean exist(Question question) {
        return exist(question.getId());
    }

    /**
     * Validate and add the question provided to the database.
     * Duplicated id will automatically change before adding.
     * Duplicated questions, where the input question matched another question with all attributes same in database, is not allowed
     *
     * @param question Question for adding
     * @author Cheung Tuen King
     * @throws Exception Duplicated question
     */
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

    /**
     * Validate and update the question provided to the database
     *
     * @param question Question for updating
     * @author Cheung Tuen King
     * @throws Exception Question id does not exist
     */
    public void updateQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }

        if (!exist(question)) {
            throw new Exception("Question \"" + question.getQuestion() + "\" does not exist.");
        }

        update(question);
    }

    /**
     * Delete the question from database
     *
     * @param question Question for deleting
     * @param deleteExam Whether exam containing question for deleting should be affected
     * @author Cheung Tuen King
     * @throws Exception Question id does not exist, or certain exam is affected but cannot be updated according to deleteExam
     */
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

            affectedExam = ExamDatabase.getInstance().queryByQuestion(question.getId());
            for (Exam exam : affectedExam) {
                exam.deleteQuestion(question);
                ExamDatabase.getInstance().updateExam(exam);
            }
        }

        delByKey(question.getId().toString());
    }

    /**
     * Delete a list of questions from database
     * @param questions List of questions for deleting
     * @param deleteExam Whether exam containing question for deleting should be affected
     * @throws Exception Question id does not exist, or certain exam is affected but cannot be updated according to deleteExam
     * @author Cheung Tuen King
     */
    public void deleteQuestions(List<Question> questions, boolean deleteExam) throws Exception {
        for (Question q : questions) {
            deleteQuestion(q, deleteExam);
        }
    }

    /**
     * Delete all questions from database.
     * Affected exam will all be updated or deleted automatically.
     * For testing purpose only.
     * @throws Exception Question id does not exist
     */
    public void deleteAll() throws Exception {
        deleteQuestions(getAll(), true);
    }
}
