package initorder;

public class FirstQuiz {
    public static void main(String[] args) {
        System.out.print("A ");
        var enumInit = EnumInit.ONE;
    }

    enum EnumInit {
        ONE;

        static {
            System.out.print("B ");
        }

        {
            System.out.print("C ");
        }

        EnumInit() {
            System.out.print("D ");
        }
    }
}
