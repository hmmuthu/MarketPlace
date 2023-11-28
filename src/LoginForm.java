import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginForm extends JDialog {
    private JPanel loginPanel;
    private JButton loginButton;
    private JButton signupButton;
    private JTextField userEmail;
    private JPasswordField password;
    private ClothingMarketPlace marketPlace;
    private User loginUser;

    public LoginForm(JFrame parent, ClothingMarketPlace marketPlace) {
        super(parent);
        marketPlace.initGui(this, "Market Place", loginPanel,500, 500);
        this.marketPlace = marketPlace;

        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });
        this.signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupAction();
            }
        });
    }

    public ClothingMarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(ClothingMarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }
    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    private void loginAction() {
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(getMarketPlace().getCustomerList());
        allUsers.addAll(getMarketPlace().getSellerList());
        this.loginUser = User.logInWithInputs(this.userEmail.getText(), String.valueOf(this.password.getPassword()), allUsers);
        if (this.loginUser != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this.loginPanel,
                    "Email or Password incorrect",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signupAction() {
        SignupForm signupForm = new SignupForm(null, getMarketPlace());
        //signupForm.pack();
        signupForm.setVisible(true);

        this.loginUser = signupForm.getLoginUser();
        if (this.loginUser != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this.loginPanel,
                    "Signup failed",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
