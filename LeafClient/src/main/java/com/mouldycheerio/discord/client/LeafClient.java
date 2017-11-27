package com.mouldycheerio.discord.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IMessage.Attachment;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.MissingPermissionsException;

public class LeafClient extends JPanel {
    /**
     *
     */
    private Color color1 = new Color(150, 255, 150, 120);
    private Color color2 = new Color(0, 100, 0, 120);
    private static final long serialVersionUID = 1L;
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
    private IDiscordClient client;
    private HashMap<String, DefaultComboBoxModel<String>> channels;
    private JComboBox<String> serversb;
    private JComboBox<String> channelsb;
    private DefaultComboBoxModel<String> servers;
    private IMessage lastMessage;
    private JTextField playing;
    private SettingsPanel settingsPanel;
    private long alpha;
    private boolean embeds = false;
    private Leaf leaf;
    private JFrame frame;
    private JPanel loginPanel;
    private JFrame loginframe;

    private BufferedImage image;
    private File profileFile;
    private int readAmmount = 64;
    private JButton logout;
    private JButton exit;
    private JButton send;
    private JButton delete;
    private JButton edit;
    private JButton reload;
    private JButton settings;
    private JPanel selections;
    private JPanel buttons;
    private JScrollPane scrollPane;

    public LeafClient(IDiscordClient client, Leaf leaf) {
        super(new BorderLayout());
        this.client = client;
        this.leaf = leaf;

        Color bgColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
        channels = new HashMap<String, DefaultComboBoxModel<String>>();
        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(this);
        loginPanel = new JPanel();
        JLabel label = new JLabel("Leaf is\n Logging in...");

        loginPanel.add(label);
        loginPanel.setBackground(bgColor);
        URL url;
        try {
            url = new URL("http://mouldycheerio.com/leaf/leaf.png");
            ImageIcon imgicon = new ImageIcon(url);
            JLabel label2 = new JLabel(imgicon);
            loginPanel.add(label2);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        loginPanel.setSize(200, 200);

        loginframe = new JFrame("Discord Leaf");
        loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginframe.setUndecorated(true);

        loginframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loginframe.setShape(new RoundRectangle2D.Double(0, 0, loginframe.getWidth(), loginframe.getHeight(), 2, 2));
            }
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        loginframe.setLocation(dim.width / 2 - loginframe.getSize().width / 2, dim.height / 2 - loginframe.getSize().height / 2);
        FrameDragListener frameDragListener = new FrameDragListener(loginframe);

        loginframe.addMouseListener(frameDragListener);
        loginframe.addMouseMotionListener(frameDragListener);
        loginframe.add(loginPanel);
        loginframe.getContentPane().setBackground(bgColor);
        loginframe.setBackground(bgColor);
        loginframe.pack();
        loginframe.setVisible(true);

    }

    public void setColors() {
        frame.setVisible(false);
        setBackground(color2);
        frame.setBackground(color2);
        buttons.setBackground(color2);
        selections.setBackground(color2);
        textArea.setBackground(color1);
        settings.setBackground(color1);
        reload.setBackground(color1);
        edit.setBackground(color1);
        delete.setBackground(color1);
        send.setBackground(color1);
        exit.setBackground(color1);
        logout.setBackground(color1);
        channelsb.setBackground(color1);
        serversb.setBackground(color1);
        textField.setBackground(color1);
        textField.setBackground(color1);
        scrollPane.setBackground(color1);
        selections.repaint();
        buttons.repaint();
        repaint();
        frame.repaint();
        frame.setVisible(true);

    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) throws IOException {
        try {
            File dir = new File("leaf_data");
            dir.mkdirs();
            profileFile = new File(dir, event.getClient().getOurUser().getStringID());
            if (profileFile.exists()) {
                FileInputStream fstream = new FileInputStream(profileFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

                String readLine = br.readLine();
                int red = Integer.parseInt(readLine);
                readLine = br.readLine();
                int green = Integer.parseInt(readLine);
                readLine = br.readLine();
                int blue = Integer.parseInt(readLine);
                readLine = br.readLine();
                int red2 = Integer.parseInt(readLine);
                readLine = br.readLine();
                int green2 = Integer.parseInt(readLine);
                readLine = br.readLine();
                int blue2 = Integer.parseInt(readLine);
                readLine = br.readLine();
                int transparency = Integer.parseInt(readLine);

                readLine = br.readLine();
                readAmmount = Integer.parseInt(readLine);

                color1 = new Color(red, green, blue, transparency);
                color2 = new Color(red2, green2, blue2, transparency);
                br.close();
                fstream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginframe.setVisible(false);
        settingsPanel = new SettingsPanel(client, this);

        textField = new JTextField(60);
        textField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                send();
            }

        });

        servers = new DefaultComboBoxModel<String>();
        for (IGuild iGuild : client.getGuilds()) {
            servers.addElement(iGuild.getName() + ": " + iGuild.getStringID());
            DefaultComboBoxModel<String> channels = new DefaultComboBoxModel<String>();
            for (IChannel iChannel : iGuild.getChannels()) {
                channels.addElement(iChannel.getName());
            }

            this.channels.put(iGuild.getName() + ": " + iGuild.getStringID(), channels);
        }

        serversb = new JComboBox<String>(servers);

        serversb.setSelectedIndex(0);
        serversb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getChannels();
                for (IGuild g : client.getGuilds()) {
                    if ((g.getName() + ": " + g.getStringID()).equals(serversb.getItemAt(serversb.getSelectedIndex()))) {
                        try {
                            File file = saveImage(g.getIconURL(), ".leaf_chache/" + g.getLongID());
                            BufferedImage b = ImageIO.read(file);
                            image = b;
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        channelsb = new JComboBox<String>();

        channelsb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectChannel(channelsb.getItemAt(channelsb.getSelectedIndex()));
            }
        });

        logout = new JButton();
        logout.setText("<");

        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.logout();
                frame.setVisible(false);
                frame.dispose();
                leaf.getFrame().setVisible(true);
            }
        });
        logout.setPreferredSize(new Dimension(32, 32));

        exit = new JButton();
        exit.setText("x");

        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.logout();
                System.exit(0);
            }
        });
        exit.setPreferredSize(new Dimension(32, 32));

        send = new JButton();
        send.setText("^");

        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
        send.setPreferredSize(new Dimension(32, 32));

        delete = new JButton();

        delete.setText("delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lastMessage != null) {
                    lastMessage.delete();
                }
            }
        });
        delete.setPreferredSize(new Dimension(64, 16));

        edit = new JButton();
        edit.setText("edit");

        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lastMessage != null) {
                    if (textField.getText().length() < 2) {
                        textField.setText(lastMessage.getContent());
                    } else {
                        lastMessage.edit(textField.getText());
                        textField.setText("");
                    }
                }
            }
        });
        edit.setPreferredSize(new Dimension(64, 16));

        reload = new JButton();

        reload.setText("rl");
        reload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectChannel(channelsb.getItemAt(channelsb.getSelectedIndex()));
            }
        });
        reload.setPreferredSize(new Dimension(32, 32));

        settings = new JButton();
        settings.setText("settings");

        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                settingsPanel.setVisible(true);
            }
        });
        settings.setPreferredSize(new Dimension(128, 16));

        textArea = new JTextArea(20, 80);
        textArea.setEditable(false);

        scrollPane = new JScrollPane(textArea);

        selections = new JPanel();
        selections.add(exit);
        selections.add(logout);
        selections.add(serversb);
        selections.add(channelsb);
        selections.add(reload);
        selections.add(settings);

        add(selections, BorderLayout.NORTH);

        scrollPane.setSize(WIDTH, HEIGHT);
        scrollPane.setBackground(color1);
        add(scrollPane, BorderLayout.CENTER);

        buttons = new JPanel();
        buttons.add(textField);
        buttons.add(send);
        buttons.add(edit);
        buttons.add(delete);

        add(buttons, BorderLayout.SOUTH);

        frame = new JFrame("Discord Leaf");
        URL url;
        try {
            url = new URL("http://mouldycheerio.com/leaf/leaf.png");
            ImageIcon imgicon = new ImageIcon(url);
            frame.setIconImage(imgicon.getImage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 6, 6));
            }
        });
        setColors();
        frame.add(this);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 3, dim.height / 3);
        FrameDragListener frameDragListener = new FrameDragListener(frame);
        frame.addMouseListener(frameDragListener);
        frame.addMouseMotionListener(frameDragListener);
        frame.pack();
        frame.setVisible(true);
        getChannels();
        while (true) {

            try {
                loops();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loops() {
        if (settingsPanel != null) {
            settingsPanel.loops();
        }
        alpha++;
        // if (alpha % 20 == 0) {
        // selectChannel(channelsb.getItemAt(channelsb.getSelectedIndex()));
        // // }
        // if (getActiveChannel() != null) {
        // if (textField.getText().length() > 1) {
        // getActiveChannel().setTypingStatus(true);
        // } else {
        // getActiveChannel().setTypingStatus(false);
        // }
        // }
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if (client.isReady()) {
            System.out.println("tee");
            addMessage(event.getMessage());
            // selectChannel(channelsb.getItemAt(channelsb.getSelectedIndex()));
        }
        // if (event.getChannel().getLongID() == getActiveChannel().getLongID()) {
        // IMessage m = event.getMessage();
        //// textArea.append("[" + m.getAuthor().getName() + "] " + m.getContent() + "\n");
        // addMessage(m);
        // }
    }

    private void getChannels() {
        for (IGuild iGuild : client.getGuilds()) {
            if (serversb.getItemAt(serversb.getSelectedIndex()).equals(iGuild.getName() + ": " + iGuild.getStringID())) {
                DefaultComboBoxModel<String> channels = new DefaultComboBoxModel<String>();
                for (IChannel iChannel : iGuild.getChannels()) {
                    channels.addElement(iChannel.getName());

                }

                channelsb.setModel(channels);
            }
        }
    }

    private void selectChannel(final String channelname) {
        new Thread(new Runnable() {

            public void run() {
                IGuild iGuild = getActiveServer();
                for (IChannel channel : iGuild.getChannels()) {
                    if ((iGuild.getName() + ": " + iGuild.getStringID()).equals(serversb.getItemAt(serversb.getSelectedIndex()))) {
                        if (channel.getName().equals(channelname)) {
                            textArea.setText("");
                            MessageHistory messageHistory = channel.getMessageHistory(readAmmount);
                            for (int i = messageHistory.size() - 1; i > -1; i--) {
                                IMessage m = messageHistory.get(i);
                                addMessage(m);
                            }
                        }
                    }
                }
            }
        }).run();

    }

    private void addMessage(IMessage m) {
        if (m.getChannel().equals(getActiveChannel())) {
            String me = "";
            if (m.getAuthor().isBot()) {
                me = "<" + m.getAuthor().getName() + "> " + m.getContent() + "\n";
            } else {
                me = "[" + m.getAuthor().getName() + "] " + m.getContent() + "\n";
            }

            if (m.getAttachments().size() > 0) {
                for (Attachment attachment : m.getAttachments()) {
                    me = me + attachment.getUrl() + "\n";
                }
            }
            textArea.setText(textArea.getText() + me);
        }
    }

    private void send() {
        String text = textField.getText();

        try {
            if (embeds) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.withTitle(text);

                lastMessage = getActiveChannel().sendMessage(eb.build());
            } else {

                for (IUser iUser : getActiveChannel().getGuild().getUsers()) {
                    if (text.contains("@" + iUser.getName() + "#" + iUser.getDiscriminator())) {
                        text = text.replace("@" + iUser.getName() + "#" + iUser.getDiscriminator(), "<@" + iUser.getStringID() + ">");
                    } else if (text.contains("@" + iUser.getName())) {
                        text = text.replace("@" + iUser.getName(), "<@" + iUser.getStringID() + ">");
                    }
                }

                lastMessage = getActiveChannel().sendMessage(text);
            }
        } catch (MissingPermissionsException e) {
            textArea.append("[Clyde] This channel has been locked, so you are unable to send messages here.");
        }
        addMessage(lastMessage);
        getActiveChannel().setTypingStatus(false);
        textField.setText("");

        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public IChannel getActiveChannel() {
        for (IGuild g : client.getGuilds()) {
            for (IChannel c : g.getChannels()) {
                if ((g.getName() + ": " + g.getStringID()).equals(serversb.getItemAt(serversb.getSelectedIndex()))) {
                    if (c.getName().equals(channelsb.getItemAt(channelsb.getSelectedIndex()))) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public IGuild getActiveServer() {
        for (IGuild g : client.getGuilds()) {
            if ((g.getName() + ": " + g.getStringID()).equals(serversb.getItemAt(serversb.getSelectedIndex()))) {
                return g;
            }
        }
        return null;
    }

    public boolean isEmbeds() {
        return embeds;
    }

    public void setEmbeds(boolean embeds) {
        this.embeds = embeds;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
//            g.drawImage(image, 0, 0, this);
        }
    }

    public static File saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
        return new File(destinationFile);
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public File getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(File profileFile) {
        this.profileFile = profileFile;
    }

}
