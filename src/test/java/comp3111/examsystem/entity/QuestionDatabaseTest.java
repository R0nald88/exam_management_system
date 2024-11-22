package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionDatabaseTest {
    private List<Question> questions;

    public void createQuestions() {
        questions = new ArrayList<>();

        try {
            Question question = new Question();
            question.setType(QuestionType.SINGLE);
            question.setOptionA("Single Answer 1");
            question.setOptionB("Single Answer 2");
            question.setOptionC("Single Answer 3");
            question.setOptionD("Single Answer 4");
            question.setQuestion("Single Question?");
            question.setScore(34);
            question.setAnswer("D");
            questions.add(question);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Question question = new Question();
            question.setType(QuestionType.MULTIPLE);
            question.setOptionA("Multiple Answer 1");
            question.setOptionB("Multiple Answer 2");
            question.setOptionC("Multiple Answer 3");
            question.setOptionD("Multiple Answer 4");
            question.setQuestion("Multiple Question?");
            question.setScore(543);
            question.setAnswer("AD");
            questions.add(question);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Question question = new Question();
            question.setType(QuestionType.SHORT_Q);
            question.setOptionA("");
            question.setOptionB("");
            question.setOptionC("");
            question.setOptionD("");
            question.setQuestion("Short Question?");
            question.setScore(123);
            question.setAnswer("Short Answer");
            questions.add(question);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Question question = new Question();
            question.setType(QuestionType.TRUE_FALSE);
            question.setOptionA("True");
            question.setOptionB("False");
            question.setOptionC("");
            question.setOptionD("");
            question.setQuestion("True False Question?");
            question.setScore(76);
            question.setAnswer("T");
            questions.add(question);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSaveQuestion() {
        try {
            QuestionDatabase.getInstance().deleteAll();
            createQuestions();
            QuestionDatabase.getInstance().addQuestion(questions.get(1));
            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q.getFirst(), questions.get(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSaveQuestions() {
        try {
            QuestionDatabase.getInstance().deleteAll();
            createQuestions();

            for (Question question: questions) {
                QuestionDatabase.getInstance().add(question);
            }

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q, questions);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            QuestionDatabase.getInstance().add(questions.get(1));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question duplicated.");
        }
    }

    @Test
    public void testUpdateQuestions() {
        testSaveQuestions();
        Question updateQuestion = questions.get(1);

        try {
            updateQuestion.setScore(234);
            QuestionDatabase.getInstance().updateQuestion(updateQuestion);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q.get(1), updateQuestion);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            updateQuestion.setId(System.currentTimeMillis());
            QuestionDatabase.getInstance().updateQuestion(updateQuestion);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q.get(1), updateQuestion);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question \"" + updateQuestion.getQuestion() + "\" does not exist.");
        }

        try {
            updateQuestion.setId(System.currentTimeMillis());
            QuestionDatabase.getInstance().updateQuestion(null);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q.get(1), updateQuestion);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question does not exist.");
        }
    }

    @Test
    public void testDeleteQuestions() {
        testSaveQuestions();
        try {
            Question a = questions.remove(1);
            QuestionDatabase.getInstance().deleteQuestion(a, false);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q, questions);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            QuestionDatabase.getInstance().deleteQuestion(null, false);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q, questions);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question does not exist.");
        }

        long id = System.currentTimeMillis();
        Question a = questions.remove(1);

        try {
            a.setId(id);
            QuestionDatabase.getInstance().deleteQuestion(a, false);

            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q, questions);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question \"" + a.getQuestion() + "\" does not exist.");
        }
    }

    @Test
    public void testFilterQuestion() {
        createQuestions();
        Question searchQuestion = questions.get(1);

        try {
            List<Question> q = QuestionDatabase.getInstance().filter(null, null, searchQuestion.getScore() + "");
            assertEquals(searchQuestion, q.get(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            List<Question> q = QuestionDatabase.getInstance().filter(null, null, searchQuestion.getScore() + "yu");
            assertEquals(searchQuestion, q.get(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(), "Score should be an integer.");
        }

    }
}
