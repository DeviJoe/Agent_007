package com.game;

import java.util.Random;

/**
 * Класс отвечает за игровую логику
 */
public class GameLogic {

    /** Отражает текущие игровые состояния */
    public enum GameStatus {
        BEFORE_START,
        GAME,
        WIN,
        FAIL
    }

    /** Отражает состояние клетки поля */
    public enum CellStatus {
        /** Клетка с числом */
        NUMBER,
        /** Клетка с бомбой */
        BOMB,
        /** Клетка, закрытая траекторией робота */
        TRAJECTORY,
        /** Клетка занята роботом */
        AGENT
    }

    /** Класс, описывающий игровую клетку */
    public class Cell {

        /**  Состояние клетки поля */
        private CellStatus cellStatus;
        /** Знаковое значение клетки (число на ней) */
        private long value;

        public Cell(CellStatus cellStatus, long value) {
            this.cellStatus = cellStatus;
            this.value = value;
        }
    }

    /** Класс описывает вектор сдвига */
    private class Vector {

        private int r;
        private int c;

        public Vector(int i, int j) {
            this.r = i;
            this.c = j;
        }
    }

    /** Полный набор игровых инструментов */


    /** Количество бомб на поле */
    private int bombsCount;
    /** Размер поля */
    private int sizeField;
    /** Игровое состояние */
    private GameStatus gameStatus = GameStatus.BEFORE_START;

    /** Координаты местонахождения робота */
    public class RobotCoordinates {
        private int robot_i;
        private int robot_j;

        public RobotCoordinates(int i, int j) {
            robot_i = i;
            robot_j = j;
        }
    }


    /** Таблица с игровым полем */
    private Cell[][] field;

    /**Координаты робота */
    private RobotCoordinates robot;

    /** Получить координаты робота */
    public RobotCoordinates getRobot() {
        return robot;
    }

    /** Устанавливает полученные значения местонахождения робота */
    public void setRobot(int robot_i, int robot_j) {
        robot.robot_i = robot_i;
        robot.robot_j = robot_j;
    }

    /** Получить поле */
    public Cell[][] getField() {
        return field;
    }

    /**
     * Генерирует случайное число от 1 до upCase
     * @param upCase верхняя граница
     * @return значение метода
     */
    private long random(int upCase) {
        long n;
        do {
            n = Math.round(Math.random() * upCase);
            return n;
        } while (n!=0);
    }

    /**
     * объект Random для генерации случайных чисел
     * (можно было бы объявить как static)
     */
    private final Random rnd = new Random();

    /**
     * Инициализирует создание игры: заполняет поле цифрами, размещает бомбы и устанавливает робота - Агента 007
     * @param bombs стартовое состояние бомб на поле
     * @param size размер квадрата игрового поля
     */
    public void start(int bombs, int size) {

        this.sizeField = size;
        this.bombsCount = bombs;

        //Заполняем поле
        field = new Cell[sizeField][sizeField];
        for (int i=0; i<sizeField; i++) {
            for (int j=0; j<sizeField; j++) {
                field[i][j] = new Cell(CellStatus.NUMBER, random(6));
            }
        }

        //Размещаем бомбы
        for (int m = 0; m < bombsCount; m++) {
            int cellIndex = rnd.nextInt(sizeField * sizeField - m);
            int k = 0;
            for (int r = 0; r < sizeField; r++) {
                for (int c = 0; c < sizeField; c++) {
                    if (field[r][c].cellStatus != CellStatus.BOMB) {
                        if (k == cellIndex) {
                            field[r][c].cellStatus = CellStatus.BOMB;
                            field[r][c].value = 0;
                            // прекращаем поиск
                            r = sizeField;
                            c = sizeField;
                        }
                        k++;
                    }
                }
            }
        }


        //Устанавливаем робота
        int a;
        int b;
        boolean b1 = false;
        do {
            long r = random(sizeField-1);
            long c = random(sizeField-1);
            if (field[(int) r][(int) c].cellStatus != CellStatus.BOMB) {
                robot.robot_i = (int) r;
                robot.robot_j = (int) c;
                b1 = true;

                field[(int) r][(int) c].cellStatus = CellStatus.AGENT;
                field[(int) r][(int) c].value = 0;
            }
        } while (b1 == false);

        gameStatus = GameStatus.GAME;
    }

    /**
     * На вход поступают параметры точки, возвращается - лежит ли точка рядом с роботом, или нет
     */
    public boolean isNearRobot(int r, int c) {

        for (int k = robot.robot_j-1; k<=robot.robot_j+1; k+=2) {
            if (r == robot.robot_i && c == k) return true;
        }

        for (int k = robot.robot_j-1; k<=robot.robot_j+1; k++) {
            if (r == robot.robot_i-1 && c == k) return true;
            if (r == robot.robot_i+1 && c == k) return true;
        }

        return false;
    }

    /**
     * Возвращает значения точки
     */
    public int numberOfCell(int r, int c) {
        return (int) field[r][c].value;
    }

    /**
     * Метод передвигает робота на n шагов вперед, закрывая пройденные клетки
     */
    public void go(int r, int c, int n) {

        //Создаем переменную, описывающую вектор сдвига робота
        Vector vector = new Vector(r-robot.robot_i, c=robot.robot_j);

        //Проходимся по всем клеткам
        int i=0;

        field[robot.robot_i][robot.robot_j].cellStatus = CellStatus.TRAJECTORY;
        field[robot.robot_i][robot.robot_j].value = 0;

        try {
            while (i<5) {

                if (field[r][c].cellStatus != CellStatus.BOMB && field[r][c].cellStatus != CellStatus.TRAJECTORY) {
                    field[r][c].cellStatus = CellStatus.TRAJECTORY;
                    field[r][c].value = 0;
                    r += vector.r;
                    c += vector.c;
                } else {
                    gameStatus = GameStatus.FAIL;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //Выход за границу массива - поражение
            gameStatus = GameStatus.FAIL;
        }

        field[r][c].cellStatus = CellStatus.AGENT;

    }

    /**
     * Последовательность действий, выполняемая при нажатии кнопки мыши по ячейке таблицы
     */
    public void mouseButtonClick(int r, int c) {

        if (isNearRobot(r, c)) {
            int n = numberOfCell(r, c);
            go(r, c, n);
        }
    }


}

