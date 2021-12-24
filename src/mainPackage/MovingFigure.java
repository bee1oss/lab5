package mainPackage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class MovingFigure implements Runnable {
    private static final int maxFramingSquareHalfSize = 60; // макс размер
    private static final int minFramingSquareHalfSize = 10; // мин размер
    private static final int minSleepTime = 1; // мин время сна

    private Field field; // исп для ост и продолж движ
    private int framingSquareHalfSize; // размер квадр
    private Color color; //цвет фигуры
    // Штриховка для рисования границы фигуры
    private Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 10.0f, new float[] {12, 3, 3, 3, 6, 3, 3, 3, 12, 3}, 0.0f);
    // Текущие координаты центра фигуры
    private double x;
    private double y;
    private int sleepTime; // время сна
    // Вертикальная и горизонтальная компоненты смещения
    private double shiftX;
    private double shiftY;

    public MovingFigure(Field field) {
        // Необходимо иметь ссылку на поле, по которому передвигается фигура,
        // чтобы отслеживать выход за его пределы через getWidth(), getHeight()
        this.field = field;
        //Случайно выбираем размер половины стороны квадрата, описывающего фигуру

        framingSquareHalfSize = minFramingSquareHalfSize + new Double(
                Math.random()*(maxFramingSquareHalfSize -
                        minFramingSquareHalfSize)).intValue();

        // Абсолютное значение времени сна потока зависит от размера фигуры,
        // чем он больше, тем больше
        sleepTime = 16 -
                new Double(Math.round(210 / framingSquareHalfSize)).intValue();
        if (sleepTime < minSleepTime)
            sleepTime = minSleepTime;

        double angle = Math.random()*2*Math.PI; // начальное движение случайно
        shiftX = 3*Math.cos(angle); // вычисляются компоненты смщения
        shiftY = 3*Math.sin(angle);

        // Цвет фигуры выбирается случайно

        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        // красный, зеленый, синий составляющей от 0-255
        // Начальное положение фигуры случайно
        x = framingSquareHalfSize + Math.random()*
                (field.getSize().getWidth()-2*framingSquareHalfSize);
        y = framingSquareHalfSize + Math.random()*
                (field.getSize().getHeight()-2*framingSquareHalfSize);

        // Создаем новый экземпляр потока, передавая аргументом
        // ссылку на объект класса, реализующего Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        thisThread.start();   // Запускаем поток
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
// то завершается и поток


    public void run() {
        try {
            // Крутим бесконечный цикл, т.е. пока нас не прервут,
            // мы не намерены завершаться
            while(true) {
                // Синхронизация потоков выполняется на объекте field.
                // Если движение разрешено - управление будет возвращено в метод.
                // В противном случае - активный поток заснет
                field.canMove(this);

                if (x + shiftX <= framingSquareHalfSize) {
                    // Достигли левой стенки, отскакиваем право
                    shiftX = -shiftX;
                    x = framingSquareHalfSize;
                } else if (x + shiftX >= field.getWidth()-
                        framingSquareHalfSize) {
                    // Достигли правой стенки, отскок влево
                    shiftX = -shiftX;
                    x=new Double(field.getWidth()-
                            framingSquareHalfSize).intValue();
                } else if (y + shiftY <= framingSquareHalfSize) {
                    // Достигли верхней стенки
                    shiftY = -shiftY;
                    y = framingSquareHalfSize;
                } else if (y + shiftY >= field.getHeight()-
                        framingSquareHalfSize) {
                    // Достигли нижней стенки
                    shiftY = -shiftY;
                    y=new Double(field.getHeight()-
                            framingSquareHalfSize).intValue();
                } else {
                    // Просто смещаемся
                    x += shiftX;
                    y += shiftY;
                }

                // Засыпаем на sleepTime миллисекунд
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем
            // и просто выходим (завершаемся)
        }
    }
    //public double  rot = 0;
    //public boolean r = true;
    //public boolean g = true;
    //public boolean b = true;
    // Метод прорисовки самой себя
    public void paint(Graphics2D canvas) {
        //Создаем объект фигуры    /// центр квадрата
        Ellipse2D ellipse  = new Ellipse2D.Double(x - framingSquareHalfSize , y - framingSquareHalfSize,
                2*framingSquareHalfSize, 2*framingSquareHalfSize);
        double x1 = x;              //нижний квадрат
        double y1 = y;
        int h1 = framingSquareHalfSize + 1;
        int w1 = framingSquareHalfSize + 1;
        Rectangle2D rec1 = new Rectangle2D.Double(x1, y1, h1, w1);

        double x2 = x - framingSquareHalfSize;
        double y2 = y - framingSquareHalfSize;
        int h2 = framingSquareHalfSize + 1;
        int w2 = framingSquareHalfSize + 1;
        Rectangle2D rec2 = new Rectangle2D.Double(x2, y2, h2, w2);

        Area figure = new Area(ellipse);
        figure.subtract(new Area(rec1));
        figure.subtract(new Area(rec2));

        //rot += Math.PI / 180;
        //AffineTransform TR =  AffineTransform.getRotateInstance(Math.PI / 4 + rot, x, y);

        //figure.transform(TR);


//        int red = color.getRed() + (r?1:-1);
  //     int green = color.getGreen() + (g?1:-1);
    //   int blue = color.getBlue() + (b?1:-1);

//       if(red == 0) { r = true; }
  //     if(green == 0 ) {g = true; }
    //   if(blue == 0 ) { b = true;}

      //  if(red == 255 ) { r = false;}
        //if(green == 255 ) {g = false;}
        //if(blue == 255 ) { b =  false;}

        //color = new Color(red, green, blue) ;
        //Задаем цвет и выполняем заливку фигуры
        canvas.setPaint(color);
        canvas.fill(figure); //fill - заливка
        ;
        //Задаем цвет и стиль линии границы и рисуем границу фигуры
        canvas.setStroke(stroke);
        canvas.setPaint(Color.black);
        canvas.draw(figure);
    }

    public int getSleepTime(){
        return sleepTime;
    }

}
