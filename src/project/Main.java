package project;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            UserLogin userLogin = new UserLogin("users.txt");
            Manager manager = new Manager("items.txt");
            GUI gui = new GUI(userLogin, manager);
            gui.setVisible(true);
        });
    }
}                                                                                         
