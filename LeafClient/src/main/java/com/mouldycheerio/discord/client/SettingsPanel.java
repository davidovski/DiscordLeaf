package com.mouldycheerio.discord.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class SettingsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private IDiscordClient client;
    private DefaultComboBoxModel<String> servers;
    private IMessage lastMessage;
    private JTextField playing;
    private LeafClient leaf;
    private JFrame frame;
    private JColorChooser tcc;
    private JCheckBox embeds;
    private JLabel tl;
    private JSlider transparency;
    private JButton button;
    private JButton button2;
    private JPanel b2;
    private JPanel b1;
    private JPanel oac;

    public SettingsPanel(IDiscordClient client, LeafClient leaf) {
        super(new GridBagLayout());
        this.client = client;
        this.leaf = leaf;
        create();
    }

    public void create() {

        playing = new JTextField(20);
        playing.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                client.changePlayingText(playing.getText());
            }

        });

        DefaultComboBoxModel<String> statuss = new DefaultComboBoxModel<String>();
        statuss.addElement("Online");
        statuss.addElement("Idle");
        statuss.addElement("Streaming");

        final JComboBox<String> status = new JComboBox<String>(statuss);
        status.setSelectedIndex(0);
        status.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });
        button = new JButton("Primary");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Color newColor = JColorChooser.showDialog(null, "Choose main color", b1.getBackground());
                b1.setBackground(newColor);
            }
        });
        button.setBounds(10, 11, 150, 23);

        button2 = new JButton("Secondary");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Color newColor = JColorChooser.showDialog(null, "Choose main color", b2.getBackground());
                b2.setBackground(newColor);
            }
        });
        button2.setBounds(10, 11, 150, 23);

        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                save(status);
            }
        });

        JButton apply = new JButton("Apply");
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save(status);
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save(status);
            }
        });

        embeds = new JCheckBox("Embeds Speak");
        embeds.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                leaf.setEmbeds(embeds.isSelected());
            }
        });


        transparency = new JSlider();
        transparency.setCursor(new Cursor(Cursor.HAND_CURSOR));
        transparency.setMajorTickSpacing(10);
        transparency.setMaximum(255);
        transparency.setMinimum(0);
        transparency.setValue(leaf.getColor1().getAlpha());
        tl = new JLabel("Client Transparency: " + transparency.getValue());



        // Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        JPanel statusPanel = new JPanel();
        statusPanel.add(status, c);
        statusPanel.add(playing, c);

        add(statusPanel, c);
        add(embeds, c);
        add(tl, c);
        add(transparency, c);
        b1 = new JPanel();
        b1.setBackground(leaf.getColor1());
        b1.add(button);
        b2 = new JPanel();
        b2.setBackground(leaf.getColor2());
        b2.add(button2);
        add(b1, c);
        add(b2, c);
        oac = new JPanel();
        oac.add(cancel, c);
        oac.add(apply, c);
        oac.add(ok, c);
        add(oac, c);
        frame = new JFrame("Discord Leaf Options");
        FrameDragListener frameDragListener = new FrameDragListener(frame);
        frame.addMouseListener(frameDragListener);
        frame.addMouseMotionListener(frameDragListener);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.add(this);
        setBackground(Color.GRAY.brighter());
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(false);
    }

    public void loops() {
        tl.setText("Client Transparency: " + transparency.getValue());
    }

    @Override
    public void setVisible(boolean vis) {
        frame.setVisible(vis);
    }

    private void save(final JComboBox<String> status) {
        leaf.setColor1(new Color(b1.getBackground().getRed(), b1.getBackground().getGreen(), b1.getBackground().getBlue(), transparency.getValue()));
        leaf.setColor2(new Color(b2.getBackground().getRed(), b2.getBackground().getGreen(), b2.getBackground().getBlue(), transparency.getValue()));;
        leaf.setColors();

        String statusString = status.getItemAt(status.getSelectedIndex());
        if ("Online".equals(statusString)) {
            if (playing.getText().length() > 1) {
                client.online(playing.getText());
            } else {
                client.online();
            }
        } else if ("Idle".equals(statusString)) {
            if (playing.getText().length() > 1) {
                client.idle(playing.getText());
            } else {
                client.idle();
            }
        } else if ("Streaming".equals(statusString)) {
            client.streaming(playing.getText(), "https://www.twitch.tv/theredturtlez");
        }
        PrintWriter pw;
        try {
            pw = new PrintWriter(leaf.getProfileFile());
            pw.println(leaf.getColor1().getRed());
            pw.println(leaf.getColor1().getGreen());
            pw.println(leaf.getColor1().getBlue());

            pw.println(leaf.getColor2().getRed());
            pw.println(leaf.getColor2().getGreen());
            pw.println(leaf.getColor2().getBlue());

            pw.println(transparency.getValue());
            pw.println(64);

            pw.close();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
