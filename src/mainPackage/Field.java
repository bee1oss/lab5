package mainPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {
    private boolean paused; // отвечает за движение или остановку
    private boolean uslpaus;

    private ArrayList<MovingFigure> figures = new ArrayList<MovingFigure>(10);
    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            // Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE); //установка фона
        repaintTimer.start(); // запуск таймера
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // Вызвать версию метода, унаследованную от предка
        Graphics2D canvas = (Graphics2D) g;
        // Последовательно запросить прорисовку от всех фигур из списка
        for (MovingFigure figure: figures)
            figure.paint(canvas);
    }


    public void addFigure() { //добавление новой фигуры
        figures.add(new MovingFigure(this)); // добавляем новый экземпляр MovingFigure
    }


    public synchronized boolean speed() {
        for (MovingFigure figure : figures) {

            if (figure.getSleepTime() > 8) {
                return true;
            }
        }
        return false;
    }


    public synchronized void Uslovie() {

        uslpaus = true;
    }

    // Cинхронизированный метод приостановки движения фигур
    // (только один поток может одновременно быть внутри)
        public synchronized void pause() {
        // Включить режим паузы
        paused = true;
    }

    // Cинхронизированный метод возобновления движения фигур
    // (только один поток может одновременно быть внутри)
    public synchronized void resume() {
        paused = false; // Выключить режим паузы
        uslpaus = false;
        notifyAll(); // Будем все ожидающие продолжения потоки
    }

    public synchronized void canMove(MovingFigure figure) throws // проверка
            InterruptedException {
        if (paused || (uslpaus && figure.getSleepTime() > 8))  // Если режим паузы включен, то поток, зашедший
            wait(); // внутрь данного метода, засыпает


    }

}