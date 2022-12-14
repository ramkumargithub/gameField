import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class Main {

    private static char playField[][];

    private static Random random = new Random();
    private static int width=0;
    private static int heigth=0;

    public static void main(String[] args) {

        Main main = new Main();
        if (null != args && args.length != 0){
            main.generatePlayField(args);
        } else {
            System.out.println("Invalid argument");
        }
    }

    private void generatePlayField(String[] args){

        int walls=0;
        int collapse=0;
        try {
            if (args.length>0)
                width = Integer.parseInt(args[0]);
            if (args.length>1)
                heigth = Integer.parseInt(args[1]);
            if(args.length>2)
                walls = Integer.parseInt(args[2]);
            if(args.length>3)
                collapse = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid format arguments");
        }
        System.out.println("width = " + width);
        System.out.println("heigth = " + heigth);
        System.out.println("walls = " + walls);
        System.out.println("collapse = " + collapse);
        playField = new char[heigth][width];
        for (char[] row: playField)
            Arrays.fill(row, '.');

        fillPlayFieldExit(playField, heigth, width);
        fillPlayFieldEntry(playField, width);
        fillPlayFieldWalls(playField, heigth, width, walls);
        fillPlayFieldCollapsePoint(playField, heigth, width, collapse);
        printPlayField(playField);

    }

    private void fillPlayFieldEntry(char playField[][], int width){

        int entry = random.nextInt(width-1);
        playField[entry][0] = '>';

    }

    private void fillPlayFieldExit(char playField[][], int heigth, int width){

        int entry = random.nextInt(heigth-1);
        playField[entry][width-1] = '<';

    }

    private void fillPlayFieldWalls(char playField[][], int heigth, int width, int walls){

        int loop = random.nextInt(1);
        if(loop == 0) {
            for (int j=0; j<walls; j++) {
                int wallElementSize = random.nextInt(5);
                int wallStartPoint = random.nextInt(width - wallElementSize -1);
                for (int i = 0; i < wallElementSize; i++) {
                    playField[wallStartPoint][i + wallStartPoint] = 'W';
                }
            }
        } else {
            for (int j=0; j<walls; j++) {
                int wallElementSize = random.nextInt(5);
                int wallStartPoint = random.nextInt(heigth - wallElementSize);
                for (int i = 0; i < wallElementSize; i++) {
                    playField[i + wallStartPoint][wallStartPoint - 1] = 'W';
                }
            }
        }
    }

    private void fillPlayFieldCollapsePoint(char playField[][], int heigth, int width, int collapse){

        for (int j=0; j<collapse;) {
            int collapsePointRow = random.nextInt(heigth-1);
            if(collapsePointRow==0) collapsePointRow++;
            int collapsePointColumn = random.nextInt(width-1);
            if(collapsePointColumn==0) collapsePointColumn++;
            char elementPresent = playField[collapsePointRow][collapsePointColumn];
            char elementPresentLeft = playField[collapsePointRow][collapsePointColumn-1];
            char elementPresentRight = playField[collapsePointRow][collapsePointColumn+1];
            char elementPresentUp = playField[collapsePointRow-1][collapsePointColumn];
            char elementPresentDown = playField[collapsePointRow+1][collapsePointColumn];
            if(elementPresent !='<' || elementPresent !='>' || elementPresent !='W'){
                playField[collapsePointRow][collapsePointColumn] = 'C';
                if (elementPresentLeft == '1') {
                    playField[collapsePointRow][collapsePointColumn-1] = '2';
                }
                else if(elementPresentLeft !='<' || elementPresentLeft !='>' || elementPresentLeft !='W'){
                    playField[collapsePointRow][collapsePointColumn-1] = '1';
                }
                if (elementPresentRight == '1') {
                    playField[collapsePointRow][collapsePointColumn+1] = '2';
                }
                else if(elementPresentRight !='<' || elementPresentRight !='>' || elementPresentRight !='W'){
                    playField[collapsePointRow][collapsePointColumn+1] = '1';
                }
                if(elementPresentUp == '1'){
                    playField[collapsePointRow-1][collapsePointColumn] = '2';
                }
                else if(elementPresentUp !='<' || elementPresentUp !='>' || elementPresentUp !='W'){
                    playField[collapsePointRow-1][collapsePointColumn] = '1';
                }
                if(elementPresentDown =='1'){
                    playField[collapsePointRow+1][collapsePointColumn] = '2';
                }
                else if(elementPresentDown !='<' || elementPresentDown !='>' || elementPresentDown !='W'){
                    playField[collapsePointRow+1][collapsePointColumn] = '1';
                }
                j++;
            }
        }
    }
    private void printPlayField(char playField[][]){
        //System.out.println("Play Field!");
        //System.out.println(Arrays.deepToString(playField));
        Stream.of(playField)
                .flatMap(Stream::of)
                .forEach(System.out::println);

    }
}

