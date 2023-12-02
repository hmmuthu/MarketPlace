import javax.swing.*;
import java.awt.*;
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
        setupLayout();
        marketPlace.initGui(this, "Market Place", loginPanel, 500, 500);
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

    private void setupLayout() {
        Font headerFont = new Font("Juice ITC", Font.BOLD, 36);
        Font defaultFont = new Font("Arial Rounded MT Bold", Font.BOLD, 16);

        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(8, 1));

        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        loginPanel.add(headerPanel);

        final JLabel headerLabel = new JLabel();
        headerLabel.setFont(headerFont);
        headerLabel.setText("Purdue Market Place");
        headerPanel.add(headerLabel);

        loginPanel.add(new JPanel());

        final JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new GridLayout(1, 2));
        loginPanel.add(emailPanel);

        final JLabel emailLabel = new JLabel();
        emailLabel.setFont(defaultFont);
        emailLabel.setText("Email");
        emailPanel.add(emailLabel);
        userEmail = new JTextField();
        emailPanel.add(userEmail);

        loginPanel.add(new JPanel());

        final JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridLayout(1, 2));
        loginPanel.add(passwordPanel);

        final JLabel passwordLabel = new JLabel();
        passwordLabel.setFont(defaultFont);
        passwordLabel.setText("Password");
        passwordPanel.add(passwordLabel);
        password = new JPasswordField();
        passwordPanel.add(password);

        loginPanel.add(new JPanel());

        final JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(1, 2));
        loginPanel.add(buttonPanel);

        loginButton = new JButton();
        loginButton.setFont(defaultFont);
        loginButton.setText("Login");
        buttonPanel.add(loginButton);

        signupButton = new JButton();
        signupButton.setFont(defaultFont);
        signupButton.setText("Signup");
        buttonPanel.add(signupButton);

        loginPanel.add(new JPanel());
    }
}
