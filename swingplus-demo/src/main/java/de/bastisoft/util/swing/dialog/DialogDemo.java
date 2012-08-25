package de.bastisoft.util.swing.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DialogDemo extends JFrame {

    private JLabel currentLabel;
    private JList<LookAndFeelInfo> lafList;
    private JButton switchButton;
    
    private List<Window> openWindows;
    
    private LookAndFeelInfo[] lafs;
    private LookAndFeelInfo currentLAF;
    
    private DialogDemo() {
        super("Dialog demo");
        
        openWindows = new ArrayList<>();
        openWindows.add(this);
        
        gatherLAFInfo();
        setContentPane(makeWidgets());
        makeActions();
        updateSwitch();
        
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void gatherLAFInfo() {
        lafs = UIManager.getInstalledLookAndFeels();
        String currentClass = UIManager.getLookAndFeel().getClass().getName();
        for (LookAndFeelInfo info : lafs)
            if (info.getClassName().equals(currentClass)) {
                currentLAF = info;
                break;
            }
    }
    
    private String currentName() {
        return currentLAF != null ? currentLAF.getName() : "<unknown>";
    }
    
    private JPanel makeWidgets() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        panel.add(lafPanel(), c);
        
        c.insets = new Insets(0, 10, 0, 0);
        panel.add(new JSeparator(JSeparator.VERTICAL), c);
        panel.add(rightPanel(), c);
        
        return panel;
    }
    
    private JPanel lafPanel() {
        JLabel l = new JLabel("Look & Feel:");
        currentLabel = new JLabel(currentName());
        switchButton = new JButton("Switch");
        
        lafList = new JList<>(lafs);
        lafList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                LookAndFeelInfo info = (LookAndFeelInfo) value;
                setText(info.getName());
                return this;
            }
        });
        
        JScrollPane scroller = new JScrollPane(lafList);
        scroller.setPreferredSize(new Dimension(200, 200));
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        panel.add(l, c);
        
        c.weightx = 1;
        c.insets = new Insets(0, 10, 0, 0);
        panel.add(currentLabel, c);
        
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(switchButton, c);
        
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 0, 0);
        panel.add(scroller, c);
        
        return panel;
    }
    
    private JPanel rightPanel() {
        JButton closeButton = linkButton("CloseDialog");
        closeButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                CloseDialogDemo dia = new CloseDialogDemo(DialogDemo.this);
                openWindows.add(dia);
                dia.setVisible(true);
            }
        });
        
        JButton okCancelButton = linkButton("OkCancelDialog");
        okCancelButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                OkCancelDialogDemo dia = new OkCancelDialogDemo(DialogDemo.this);
                openWindows.add(dia);
                dia.run();
            }
        });
        
        JButton statusHeaderButton = linkButton("StatusHeader");
        statusHeaderButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                StatusHeaderDemo dia = new StatusHeaderDemo(DialogDemo.this);
                openWindows.add(dia);
                dia.setVisible(true);
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(closeButton, c);
        c.insets = new Insets(3, 0, 0, 0);
        panel.add(okCancelButton, c);
        
        c.weighty = 1;
        panel.add(statusHeaderButton, c);
        
        return panel;
    }
    
    private JButton linkButton(String label) {
        JButton b = new JButton("<html><u>" + label);
        b.setForeground(Color.RED);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    
    private void makeActions() {
        lafList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSwitch();
            }
        });
        
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchAction();
            }
        });
    }
    
    private void updateSwitch() {
        LookAndFeelInfo selected = lafList.getSelectedValue();
        switchButton.setEnabled(selected != null && selected != currentLAF);
    }
    
    private void switchAction() {
        LookAndFeelInfo selected = lafList.getSelectedValue();
        if (selected != null) {
            currentLAF = selected;
            currentLabel.setText(currentLAF.getName());
            updateSwitch();
            
            try {
                UIManager.setLookAndFeel(currentLAF.getClassName());
                
                for (Iterator<Window> it = openWindows.iterator(); it.hasNext();) {
                    Window win = it.next();
                    if (win.isVisible()) {
                        SwingUtilities.updateComponentTreeUI(win);
                        win.pack();
                    }
                    else
                        it.remove();
                }
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                JOptionPane.showMessageDialog(this, e.toString(), "Error setting L&F", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        new DialogDemo();
    }
    
}
