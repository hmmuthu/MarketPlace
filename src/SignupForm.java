import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SignupForm extends JDialog {
    private JPanel signupPanel;
    private JButton signupButton;
    private JTextField userEmail;
    private JPasswordField password;
    private JComboBox userRole;
    private User loginUser;

    private ClothingMarketPlace marketPlace;
    public SignupForm(JFrame parent, ClothingMarketPlace marketPlace) {
        super(parent);
        marketPlace.initGui(this, "Signup", signupPanel,500, 500);
        this.userRole.addItem("Customer");
        this.userRole.addItem("Seller");
        this.marketPlace = marketPlace;
        this.signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpAction();
            }
        });
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public ClothingMarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(ClothingMarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    private void signUpAction() {
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(this.marketPlace.getCustomerList());
        allUsers.addAll(this.marketPlace.getSellerList());
        this.loginUser = User.signupWithInputs(
                this.userEmail.getText(),
                String.valueOf(this.password.getPassword()),
                this.userRole.getSelectedIndex() == 0,
                allUsers);
        if (this.loginUser != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(signupPanel,
                    "Email already taken",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
