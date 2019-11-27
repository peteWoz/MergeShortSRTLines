/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package main.use.it.free;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;


public class MergeSubsMain extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton, goButton;
    JTextArea log;
    JFileChooser fc;
    SubTitleController subTitleController;
    List<TextStructModel> result;
    JComboBox maxDuration;
    int limit = 7;
    File sourceFile;
    boolean goPressed = false;

    public MergeSubsMain() {
        super(new BorderLayout());

        //Create the log first, because the action listeners need to refer to it.
        log = new JTextArea(15, 50);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        openButton = new JButton("Open a File");
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File");
        saveButton.setEnabled(goPressed);
        saveButton.addActionListener(this);

        goButton = new JButton(" Go ");
        //goButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        goButton.addActionListener(this);

        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        maxDuration = new JComboBox(numbers);
        maxDuration.setSelectedIndex(limit - 1);
        maxDuration.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(goButton);

        JPanel parametersPanel = new JPanel();
        parametersPanel.add(new JLabel("Max duration of merged lines: "));
        parametersPanel.add(maxDuration);
        parametersPanel.add(new JLabel("sec."));
        top.add(buttonPanel);
        top.add(parametersPanel);

        //Add the buttons and the log to this panel.
        add(top, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        log.append("1. Start by opening an SRT file.\n");
        log.append("2. Pick your preferred 'max duration of merged lines' limit, which by default is set to " + limit + "sec.\n");
        log.append("3. Press the GO button.\n");
        log.append("4. And click the 'Save a File' button.\n \n");
    }

//    private Path getFolderPath() throws URISyntaxException, IOException {
//        URI uri = getClass().getClassLoader().getResource("folder").toURI();
//        if ("jar".equals(uri.getScheme())) {
//            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap(), null);
//            return fileSystem.getPath("path/to/folder/inside/jar");
//        } else {
//            return Paths.get(uri);
//        }
//    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new MergeSubsMain());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread: creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(MergeSubsMain.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                subTitleController = new SubTitleController(limit);
                sourceFile = fc.getSelectedFile();
                result = subTitleController.readFile(sourceFile.getAbsolutePath());
                //This is where a real application would open the file.
                log.append("Opening: " + sourceFile.getAbsolutePath() + "." + newline);
                log.append("The SRT file has " + subTitleController.initialNoOfLines + " lines.\n \n");
                log.append("Now pick your preferred max duration of merged lines and then press 'Go'.\n \n");
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == goButton) {
            if (sourceFile == null) {
                log.append("Please first select an SRT file.");
                return;
            }
            // start fresh in-case the limit has changed
            subTitleController = new SubTitleController(limit);
            result = subTitleController.readFile(sourceFile.getAbsolutePath());

            subTitleController.setAcceptableMergedDuration(limit);
            result = subTitleController.mergeShortLines(result);
            System.out.println("Merged Lines - first run completed \n");
            result = subTitleController.mergeShortLines(result);
            System.out.println("Merged Lines - second run completed \n");
            result = subTitleController.mergeShortLines(result);
            goPressed = true;
            saveButton.setEnabled(goPressed);
            System.out.println("Merged Lines - third run completed \n");
            log.append("The SRT file has been processed and it has reduced to " + subTitleController.finalNumberOfLines + " lines. \nNow choose where to save it.\n \n");
            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(MergeSubsMain.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.append("Your file has been saved to: " + file.getAbsolutePath() + "." + newline);
                subTitleController.saveFileTo(file.getAbsolutePath(), result);
                goPressed = false;
                saveButton.setEnabled(goPressed);
                log.append("------------------------------------------------------------------------------------------------------------------------------ \n \n");
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == maxDuration) {
            limit = (int) maxDuration.getSelectedItem();
            log.append("Limit is set to: " + limit + "sec. \n\n");
        }
    }
}
