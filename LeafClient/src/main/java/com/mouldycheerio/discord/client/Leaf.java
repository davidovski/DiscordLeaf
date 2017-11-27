package com.mouldycheerio.discord.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class Leaf {

    private IDiscordClient client;
    private EventDispatcher dispatcher;
    private Object eventListener;
    private JTextField textField;
    private DefaultComboBoxModel<String> profileslist;
    private JComboBox<String> profilesBox;

    private HashMap<String, String> profiles;
    private JFrame frame;
    private String selected;

    public static void main(String[] args) {
        new Leaf().run();

    }

    public void run() {
        profiles = new HashMap<String, String>();

        FileInputStream fstream;
        try {
            File f = new File(".leaf_profiles");
            fstream = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String readLine = br.readLine();
            for (String string : readLine.split("&")) {
                String[] split = string.split(">");
                profiles.put(split[0], split[1]);
            }
            String readLine2 = br.readLine();
            selected = readLine2;
            br.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // String text = JOptionPane.showInputDialog(null, "Enter token", "Leaf", JOptionPane.QUESTION_MESSAGE);
        // login(text);

        JPanel loginScreen = new JPanel(new GridBagLayout());
        profileslist = new DefaultComboBoxModel<String>();
        // profileslist.addElement("Select Profile");
        for (Entry<String, String> entry : profiles.entrySet()) {
            profileslist.addElement(entry.getKey());
        }

        if (profileslist.getSize() < 1) {
            profileslist.addElement("Select Profile");
        }

        profilesBox = new JComboBox<String>(profileslist);
        profilesBox.setSelectedItem(selected);
        profilesBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });

        JButton login = new JButton();
        login.setText("login");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                String name = profilesBox.getItemAt(profilesBox.getSelectedIndex());
                if (profiles.containsKey(name)) {
                    saveProfiles();
                    IDiscordClient iDiscordClient = login(profiles.get(name));

                    start(iDiscordClient);

                }
            }
        });

        JButton newProfile = new JButton();
        newProfile.setText("new profile");
        newProfile.addActionListener(new java.awt.event.ActionListener() {
            private JTextField tokenField;
            private JTextField textField2;
            private JLabel l;
            private JFrame newFram;

            public void actionPerformed(ActionEvent e) {
                // String text = JOptionPane.showInputDialog(null, "Enter token", "Leaf", JOptionPane.QUESTION_MESSAGE);
                JPanel newProfileScreen = new JPanel();

                textField2 = new JTextField(20);
                textField2.setText("new profile");

                tokenField = new JTextField(20);
                tokenField.setText("token");
                tokenField.selectAll();

                l = new JLabel("Create profile");
                JButton x = new JButton();
                x.setText("x");
                x.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        newFram.setVisible(false);
                        newFram.dispose();
                    }

                });

                JButton create = new JButton();
                create.setText("create");
                create.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        IDiscordClient c = login(tokenField.getText());
                        try {
                            c.login();
                            profiles.put(textField2.getText(), tokenField.getText());
                            profileslist = new DefaultComboBoxModel<String>();
                            for (Entry<String, String> entry : profiles.entrySet()) {
                                profileslist.addElement(entry.getKey());

                            }
                            profilesBox.setModel(profileslist);

                            saveProfiles();

                            newFram.setVisible(false);
                            newFram.dispose();
                        } catch (Exception ex) {
                            l.setText("this token in invalid (failed login)");
                        }

                        try {
                            c.logout();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                });
                x.setPreferredSize(new Dimension(16, 16));

                newProfileScreen.add(x);
                newProfileScreen.add(textField2);
                newProfileScreen.add(tokenField);

                newProfileScreen.add(create);
                newProfileScreen.add(l);

                newFram = new JFrame("Create Profile");
                newFram.setUndecorated(true);

                newFram.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        newFram.setShape(new RoundRectangle2D.Double(0, 0, newFram.getWidth(), newFram.getHeight(), 2, 2));
                    }
                });
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                newFram.setLocation(dim.width / 2 - newFram.getSize().width / 2, dim.height / 2 - newFram.getSize().height / 2);
                newFram.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                newFram.add(newProfileScreen);
                newProfileScreen.setPreferredSize(new Dimension(300, 150));
                Color bgColor = new Color(1.0f,1.0f,1.0f,0.5f);
                FrameDragListener frameDragListener = new FrameDragListener(newFram);
                newFram.addMouseListener(frameDragListener);
                newFram.addMouseMotionListener(frameDragListener);
                newFram.getContentPane().setBackground(bgColor);
                newFram.setBackground(bgColor);

                newFram.pack();
                newFram.setVisible(true);

                // IDiscordClient client = login(text);
            }
        });

        loginScreen.add(new JLabel("Leaf Login"));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        loginScreen.add(profilesBox, c);
        loginScreen.add(login, c);
        loginScreen.add(newProfile, c);
        frame = new JFrame("Leaf login");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginScreen.setPreferredSize(new Dimension(400, 200));
        frame.add(loginScreen);
        loginScreen.setBackground(Color.GRAY);
        URL url;
        try {
            url = new URL("http://mouldycheerio.com/leaf/leaf.png");
            ImageIcon imgicon = new ImageIcon(url);
            frame.setIconImage(imgicon.getImage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(login);

    }

    public IDiscordClient login(String token) {

        try {
            IDiscordClient client = ClientFactory.createClient(token, false);
            return client;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean start(IDiscordClient client) {
        client.login();
        LeafClient main = new LeafClient(client, this);
        return true;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    private void saveProfiles() {
        String f = "";
        for (Entry<String, String> entry : profiles.entrySet()) {
            f = f + entry.getKey() + ">" + entry.getValue() + "&";
        }

        try {
            FileOutputStream out = new FileOutputStream(".leaf_profiles");
            PrintWriter writer = new PrintWriter(".leaf_profiles", "UTF-8");
            writer.println(f);
            writer.println(profilesBox.getSelectedItem());
            writer.close();
            out.close();
        } catch (IOException ex) {
            // do something
        }
    }

}
