package mainPackage;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    // Константы, задающие размер окна приложения, если оно
    // не распахнуто на весь экран
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private JMenuItem uslPauseMenuItem;
    // Поле, по которому движутся фигуры
    private Field field = new Field();


    // Конструктор главного окна приложения
    public MainFrame() {
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        // Установить начальное состояние окна развернутым на весь экран
        setExtendedState(MAXIMIZED_BOTH);

        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu figuresMenu = new JMenu("Фигуры");
        Action addFigureAction = new AbstractAction("Добавить фигуру") {
            public void actionPerformed(ActionEvent event) {
                field.addFigure();
                if (!pauseMenuItem.isEnabled() &&
                        !resumeMenuItem.isEnabled()) {
                    // Ни один из пунктов меню не является
                    // доступным - сделать доступным "Паузу"
                    pauseMenuItem.setEnabled(true);
                    uslPauseMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(figuresMenu);
        figuresMenu.add(addFigureAction);

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);

        Action UslPause = new AbstractAction("Условная пауза"){
            public void actionPerformed(ActionEvent event) {
                if(field.speed() == true) {
                    field.Uslovie();
                    uslPauseMenuItem.setEnabled(false);
                    pauseMenuItem.setEnabled(true);
                    resumeMenuItem.setEnabled(true);
                }
                else{
                    field.Uslovie();
                    uslPauseMenuItem.setEnabled(true);
                    pauseMenuItem.setEnabled(true);
                    resumeMenuItem.setEnabled(false);
                }
            }
        };

        uslPauseMenuItem = controlMenu.add(UslPause);
        uslPauseMenuItem.setEnabled(false);

        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                uslPauseMenuItem.setEnabled(false);
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);

        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                uslPauseMenuItem.setEnabled(true);
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        // Добавить в центр граничной компоновки поле Field
        getContentPane().add(field, BorderLayout.CENTER);
    }

    // Главный метод приложения
    public static void main(String[] args) {
        // Создать и сделать видимым главное окно приложения
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
