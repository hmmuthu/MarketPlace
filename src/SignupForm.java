import javax.swing.*;
import java.awt.*;
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
        setupLayout();
        marketPlace.initGui(this, "Signup", signupPanel, 500, 500);
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

    private void setupLayout() {
        Font headerFont = new Font("Juice ITC", Font.BOLD, 36);
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(10, 1));

        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        signupPanel.add(headerPanel);

        final JLabel headerLabel = new JLabel();
        headerLabel.setFont(headerFont);
        headerLabel.setText("Signup");
        headerPanel.add(headerLabel);

        signupPanel.add(new JPanel());

        final JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new GridLayout(1, 2));
        signupPanel.add(emailPanel);

        final JLabel emailLabel = new JLabel();
        emailLabel.setFont(defaultFont);
        emailLabel.setText("Email");
        emailPanel.add(emailLabel);
        userEmail = new JTextField();
        emailPanel.add(userEmail);

        signupPanel.add(new JPanel());

        final JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridLayout(1, 2));
        signupPanel.add(passwordPanel);

        final JLabel passwordLabel = new JLabel();
        passwordLabel.setFont(defaultFont);
        passwordLabel.setText("Password");
        passwordPanel.add(passwordLabel);
        password = new JPasswordField();
        passwordPanel.add(password);

        signupPanel.add(new JPanel());

        userRole = new JComboBox();
        signupPanel.add(userRole);

        signupPanel.add(new JPanel());

        signupButton = new JButton();
        signupButton.setFont(defaultFont);
        signupButton.setText("Signup");
        signupPanel.add(signupButton);

        signupPanel.add(new JPanel());
    }


}
