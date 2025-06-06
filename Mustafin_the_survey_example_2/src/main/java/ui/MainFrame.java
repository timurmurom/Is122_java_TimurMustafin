package ui;
import javax.swing.*;
import controllers.UserController;
import controllers.SurveyController;
import models.User;
import services.SurveyInitializer;
import services.SurveyService;
import models.Survey;


// Принцип единственной ответственности и Паттерн Фасад


public class MainFrame extends JFrame {
    private SurveyController surveyController;
    private UserController userController; // Сохраняем экземпляр UserController

    public MainFrame() {
        setTitle("Анкетирование");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        this.userController = new UserController(); // Инициализируем здесь
        this.surveyController = new SurveyController(new SurveyService());
        new SurveyInitializer(surveyController).initializeSurveys();



        RegisterPanel registerPanel = new RegisterPanel(this, surveyController, userController);
        setContentPane(registerPanel);
        revalidate();
        setVisible(true);
        setVisible(true);
    }

    public void showMainMenu(){
        getContentPane().removeAll();
        LoginPanel loginPanel = new LoginPanel(this, userController, surveyController);
        add(loginPanel);
        revalidate();
        repaint();
    }

    public SurveyController getSurveyController() {
        return surveyController;
    }

    public UserController getUserController() {
        return userController;
    }

    public void startSurvey(){
        //Запускаем анкету после регистрации/входа
        Survey survey = surveyController.getSurveys().get(0);
        SurveyPanel surveyPanel = new SurveyPanel(this, new User(0, "User", "User_u@internet.ru", "D8_t5-K122"),surveyController, survey);
        setContentPane(surveyPanel);
        revalidate();
        repaint();
    }


    // Основной метод запуска программы
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());

    }
}
