package texteditor;

import com.sun.glass.events.KeyEvent;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.font.TextAttribute;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.WindowConstants.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class TextEditor extends JPanel implements ActionListener {
    static JFrame f = new JFrame();
    JFrame FindReplace;
    static JMenuBar MenuBar = new JMenuBar();
    static JTextPane Text = new JTextPane();
    static JPanel Bottom = new JPanel();
    static String fName = "";
    static File file;
    static String fileText;
    static JScrollPane jp = new
    JScrollPane(Text,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    int initial = 0,init,len;
    static String sText = "";
    String input;
    static String FontName = "Arial";
    static int TextSize = 11;
    static String TextCase = "None";
    static String TextFormat = "None";
    static int emphasis = 0; //0 - no emphasis
                             //1 - bold
                             //2 - italics
                             //3 - underlined
    static StyledDocument doc = (StyledDocument)Text.getDocument();
    static int toBold = 0;
    static int toItalics = 0;
    static int toUnderline = 0;    
    
    TextEditor()
    {
        f.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection cText;

        f.setTitle("Untitled - Feather N' Ink Text Editor");

        //For Top Menu.
        Text.setEditable(false);
        JMenu File = new JMenu("File");
        JMenu Edit = new JMenu("Edit");
        JMenu Format = new JMenu("Format");
        JMenu Tools = new JMenu("Tools");
        
        JMenuItem New = new JMenuItem("New");
        JMenuItem Save = new JMenuItem("Save");
        JMenuItem Load = new JMenuItem("Load");

        JMenuItem Cut = new JMenuItem("Cut");
        JMenuItem Copy = new JMenuItem("Copy");
        JMenuItem Paste = new JMenuItem("Paste");
        JMenuItem Find = new JMenuItem("Find");

        JMenuItem Fontt = new JMenuItem("Font");
        
        JMenuItem WordCount = new JMenuItem("Word Count");
        
        File.add(New);
        File.add(Save);
        File.add(Load);

        Edit.add(Cut);
        Edit.add(Copy);
        Edit.add(Paste);
        Edit.add(Find);

        Format.add(Fontt);
        
        Tools.add(WordCount);

        MenuBar.add(File);
        MenuBar.add(Edit);
        MenuBar.add(Format);
        MenuBar.add(Tools);


        Text.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(Text.getSelectedText()!=null)
                {
                    sText = Text.getSelectedText();                    
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JFrame newFrame = new JFrame();
                JPanel p1 = new JPanel();
                JPanel p2 = new JPanel();
                JPanel p3 = new JPanel();

                newFrame.setSize(300,130);
                newFrame.setTitle("New File");
                
                p1.add(new JLabel("Enter File Name (With File Fxtension)"));
                JTextField newText = new JTextField(9);

                p2.add(newText);

                JButton okay = new JButton("Okay");
                okay.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Text.setText("");
                        FontName = "Arial";
                        TextSize = 11;
                        TextCase = "None";
                        TextFormat = "None";
                        
                        fName = newText.getText();
                        f.setTitle(fName);
                        try
                        {
                            CreateFile();
                            newFrame.dispose();
                        }
                        catch (IOException ex)
                        {
                            System.out.println("File not made.");
                        }
                    }
                });
                p3.add(okay);

                newFrame.add(p1,BorderLayout.NORTH);
                newFrame.add(p2,BorderLayout.CENTER);
                newFrame.add(p3,BorderLayout.SOUTH);
                newFrame.setLocationRelativeTo(null);
                newFrame.setResizable(false);
                newFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
                newFrame.setVisible(true);
            }
        });

        Save.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedWriter writer = null;
                String fileText = Text.getText();
                try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(fileText);
                }
                catch (IOException ex) {
                    System.err.println(ex);
                }
                finally {
                    if (writer != null)
                    {
                        try {
                            writer.close();
                            }
                        catch (IOException ex) {
                            System.err.println(ex);
                            }
                    }
                }
        }});

        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loadFrame = new JFrame();
                JPanel p1 = new JPanel();
                JPanel p2 = new JPanel();
                JPanel p3 = new JPanel();

                loadFrame.setSize(300,130);
                loadFrame.setTitle("Load File");
                
                p1.add(new JLabel("Enter File Name (With file Extension)"));

                JTextField loadText = new JTextField(9);
                p2.add(loadText);

                JButton ok = new JButton("Okay");
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fName = loadText.getText();
                        String fName2 = fName;
                        try{
                            OpenFile();
                            f.setTitle(fName2);
                            Text.setEditable(true);
                            loadFrame.dispose();
                        }
                        catch(IOException ex){
                            System.out.println("Can't Open File");
                        }
                    }

                });

                p3.add(ok);

                loadFrame.add(p1,BorderLayout.NORTH);
                loadFrame.add(p2,BorderLayout.CENTER);
                loadFrame.add(p3,BorderLayout.SOUTH);
                loadFrame.setResizable(false);
                loadFrame.setLocationRelativeTo(null);
                loadFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
                loadFrame.setVisible(true);
            }
        });

        Copy.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Text.getSelectedText()!=null)
                {
                    sText = Text.getSelectedText();
                    StringSelection cText= new StringSelection(sText);
                    clipboard.setContents(cText, null);
                }
            }
        });

        Cut.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Text.getSelectedText()!=null)
                {
                    sText = Text.getSelectedText();
                    StringSelection cText= new StringSelection(sText);
                    clipboard.setContents(cText, null);
                    Text.replaceSelection("");
                }
            }
        });

        Paste.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Text.paste();
            }
        });

        Fontt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame fontFrame = new JFrame();
                fontFrame.setTitle("Font");
                fontFrame.setLayout(new GridLayout(3,1));

                JPanel p1 = new JPanel();
                JPanel p2 = new JPanel();
                JPanel p3 = new JPanel();
                JPanel p5 = new JPanel();
                JPanel p6 = new JPanel();

                p1.setLayout(new GridLayout(2,3,10,5));
                p5.setLayout(new GridLayout(2,1,0,5));  //Label Emphasis and p6
                
                fontFrame.setSize(500,230);

                p1.add(new JLabel("Font"));
                p1.add(new JLabel("Size"));
                p1.add(new JLabel("Case"));
                
                String[] fonts = {"Agency FB", "Algerian", "Arial", "Arimo",
                    "Baskerville Old Face","Bell MT", "Broadway", "Calibri", "Cambria", "Candara", 
                    "Castellar", "Courier New", "DialogInput",
                    "DejaVu Sans","Garamond", "Georgia", "Helvetica","JokerMan", "Loose Script", "Lucida Handwriting",
                    "Monospaced", "Sans Serif","Serif", "Times New Roman"};
                
                JComboBox fontType = new JComboBox(fonts);
                p1.add(fontType);
                
                JComboBox<Integer> size = new JComboBox<Integer>();
                for(int i = 10; i<=100; i = i+2)
                    size.addItem(i);
                p1.add(size);

                String[] cases = {"None", "lower case", "UPPER CASE", "tOGGLE cASE"};
                JComboBox caseOfText = new JComboBox(cases);
                p1.add(caseOfText);
                                
                JButton BoldButton = new JButton("B");
                JButton ItalicsButton = new JButton("I");
                JButton UnderlinedButton = new JButton("U");
                                
                Font boldButtonFont = new Font(BoldButton.getFont().getName(), Font.BOLD, BoldButton.getFont().getSize());
                Font italicsButtonFont = new Font(ItalicsButton.getFont().getName(), Font.ITALIC, ItalicsButton.getFont().getSize());
                
                BoldButton.setFont(boldButtonFont);
                ItalicsButton.setFont(italicsButtonFont);
                UnderlinedButton.setMnemonic(KeyEvent.VK_U);
                
                p6.setLayout(new GridLayout(1,3,3,0));
                p6.add(BoldButton);
                p6.add(ItalicsButton);
                p6.add(UnderlinedButton);
        
                p5.add(new JLabel("Emphasis"));
                p5.add(p6);
                
                JButton okay = new JButton("Okay");
                
                JPanel top = new JPanel();
                JPanel middle = new JPanel();
                JPanel down = new JPanel();
                
                top.add(p1);                
                middle.add(p5);
                down.add(okay);
                
                fontFrame.add(top);
                fontFrame.add(middle);
                fontFrame.add(down);
                
                fontType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FontName = (String)fontType.getSelectedItem();
                        try{
                            ChangeFont();
                        }
                        catch (BadLocationException ex)
                        {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                size.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TextSize = (Integer)size.getSelectedItem();
                        try
                        {
                            ChangeFont();
                        }
                        catch (BadLocationException ex) {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                caseOfText.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TextCase = (String)caseOfText.getSelectedItem();
                        
                        try 
                        {
                            ChangeFont();
                        } 
                        catch (BadLocationException ex) 
                        {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                BoldButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        emphasis = 1;
                        
                        if(toBold == 0)
                            toBold = 1;
                        else
                            toBold = 0;
                        
                        try 
                        {
                            ChangeFont(); 
                        } 
                        catch (BadLocationException ex) {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                ItalicsButton.addActionListener(new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        emphasis = 2;
                        
                        if(toItalics == 0)
                           toItalics = 1;
                        else
                            toItalics = 0;
                        
                        try
                        {
                            ChangeFont(); 
                        } 
                        catch (BadLocationException ex)
                        {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                                
                UnderlinedButton.addActionListener(new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        emphasis = 3;
                        
                        if(toUnderline == 0)
                           toUnderline = 1;
                        else
                            toUnderline = 0;
                        
                        try
                        {
                            ChangeFont();
                        } 
                        catch (BadLocationException ex) {
                            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                okay.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fontFrame.dispose();
                        sText = "";
                        FontName = "Arial";
                        TextSize = 11;
                        TextCase = "None";
                        TextFormat = "None";
                        emphasis = 0;
                        toBold = 0;
                        toItalics = 0;
                        toUnderline = 0;
                    }
                });
                
                fontFrame.setResizable(false);
                fontFrame.setLocationRelativeTo(null);
                fontFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
                fontFrame.setVisible(true);
            }
        });

        Find.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindReplace = new JFrame();
                JPanel j1= new JPanel();
                JPanel j2  = new JPanel();
                JPanel j3  = new JPanel();
                JPanel j4  = new JPanel();

                JLabel Find = new JLabel("Find What");
                JTextField SearchBar = new JTextField();
                SearchBar.setColumns(13);
                JButton Next = new JButton("Next");

                JLabel ReplaceWith = new JLabel("Replace With");
                JTextField ReplaceBar = new JTextField();
                ReplaceBar.setColumns(13);
                JButton ReplaceNext = new JButton("Replace Next");
                JButton ReplaceAll = new JButton("Replace All");


                Next.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                }});

                Next.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        String total = Text.getText();
                        input = SearchBar.getText();
                        len = input.length();
                        initial = total.indexOf(input, initial);
                        if(total.contains(input))
                        {
                            if(initial==0)
                            Text.select(initial, initial+len+1);
                            else
                            Text.select(initial, initial+len);

                            initial = initial+len;
                        }
                    }
                });

                ReplaceNext.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String total = Text.getText();
                        input = SearchBar.getText();
                        len = input.length();

                        if(total.contains(input))
                        {
                            initial = total.indexOf(input, initial);
                            String re = ReplaceBar.getText();
                            if(initial==0)
                            Text.select(initial, initial+len+1);
                            else
                            Text.select(initial, initial+len);
                            Text.replaceSelection(re);
                            initial = initial+len;
                        }
                    }
                });

                ReplaceAll.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        String total = Text.getText();
                        input = SearchBar.getText();
                        total = total.replace(input, ReplaceBar.getText());
                        Text.setText(total);
                    }
                });
            
                j1.add(Find);
                j1.add(SearchBar);
                j1.add(Next);

                j2.add(ReplaceWith);
                j2.add(ReplaceBar);
                j2.add(ReplaceNext);
                j3.add(ReplaceAll);

                FindReplace.setLayout(new FlowLayout());
                FindReplace.add(j1);
                FindReplace.add(j2);
                FindReplace.add(j3);
                FindReplace.setSize(680,125);
                FindReplace.setLocationRelativeTo(null);
                FindReplace.setResizable(false);
                FindReplace.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
                FindReplace.setVisible(true);
                FindReplace.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }});
        
        WordCount.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame WordFrame = new JFrame();
                WordFrame.setTitle("Word Count");
                WordFrame.setSize(300, 130);
              
                String[] wordsInSelectedText = sText.split(" |\\\n");

                int NoOfSelectedWords = 0;                                  
                for(int i = 0; i < wordsInSelectedText.length; i++)        //Number of Words selected
                {
                    if(wordsInSelectedText[i].length() != 0)
                        NoOfSelectedWords++;
                }

                String textword = Text.getText();
                String[] wordsInText = Text.getText().split(" |\\\n");

                int NoOfWords = 0;                                  
                for(int i = 0; i < wordsInText.length; i++)        //Number of Words
                {
                    if(wordsInText[i].length() != 0)
                        NoOfWords++;
                }
                
                int characters = textword.length();     //Number of characters, with spaces

                String[] CharWoSpace = textword.split(" ");     //Number of characters, without space
                int NumCharWoSpace = 0;
                for(int i = 0; i < CharWoSpace.length; i++)
                {
                    NumCharWoSpace = NumCharWoSpace + CharWoSpace[i].length();
                }
                
                String sw = "Selected Words: ";
                sw = sw + NoOfSelectedWords;
                
                String w = "Words: \t";
                w = w + NoOfWords;

                String CharSpace = "Characters (with Spaces): ";
                CharSpace = CharSpace + characters;

                String CharWithoutSpace = "Characters (without Spaces): ";
                CharWithoutSpace = CharWithoutSpace + NumCharWoSpace;

                JLabel selectedWords = new JLabel(sw);
                JLabel words = new JLabel(w);
                JLabel charactersW = new JLabel(CharSpace);
                JLabel charactersWO = new JLabel(CharWithoutSpace);

                JButton okay = new JButton("Okay");
                okay.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WordFrame.dispose();
                    }
                });
                WordFrame.setLayout(new GridLayout(5,1));

                WordFrame.add(selectedWords);
                WordFrame.add(words);
                WordFrame.add(charactersW);
                WordFrame.add(charactersWO);
                WordFrame.add(okay);

                WordFrame.setLocationRelativeTo(null);
                WordFrame.setResizable(false);
                WordFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FeatherNInk.jpg")));
                WordFrame.setVisible(true);
                WordFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }
            });
    }
    
        
    
    public static void ChangeFont() throws BadLocationException
    {
        String caseString = "";
        int init = Text.getCaretPosition()-sText.length();
        doc.remove(init, sText.length());
        
        SimpleAttributeSet normal = new SimpleAttributeSet(); 
        StyleConstants.setFontFamily(normal,FontName);
        StyleConstants.setFontSize(normal, TextSize);
        
        if(TextCase.equalsIgnoreCase("lower case"))
        {
            caseString = sText.toLowerCase();
        }
        else if(TextCase.equalsIgnoreCase("UPPER CASE"))
        {
            caseString = sText.toUpperCase();
        }
        else if(TextCase.equalsIgnoreCase("tOGGLE cASE"))
        {
            int i;
            char c;
            for(i = 0; i<sText.length();i++ )
            {
                c = sText.charAt(i);
                if(Character.isUpperCase(c))
                {
                    c = Character.toLowerCase(c);
                }
                else if(Character.isLowerCase(c))
                {
                    c = Character.toUpperCase(c);
                }
                caseString = caseString + c;
            }
        }
        else if(TextCase.equalsIgnoreCase("None"))
        {
            caseString = sText;
        }
        
        SimpleAttributeSet emphasisText = new SimpleAttributeSet(normal);
        if(emphasis == 0)
        {
            doc.insertString(init, caseString, normal);
        }
        else
        {
            if(toBold == 1)
            {
                StyleConstants.setBold(emphasisText, true);
            }

            if(toItalics == 1)
            {
                StyleConstants.setItalic(emphasisText, true);
            }

            if(toUnderline == 1)
            {
                StyleConstants.setUnderline(emphasisText, true);
            }

            doc.insertString(init, caseString, emphasisText);
        }
    }

    public static void CreateFile() throws IOException
    {
        new File("C://Text Editor").mkdirs();
        String Name = "C://Text Editor//" + fName;
        file = new File(Name);
        if(file.createNewFile())
        {
            System.out.println("File Created");
            Text.setEditable(true);
        }
    }
    
    public static void OpenFile() throws IOException
    {
        String Name = "C://Text Editor//" + fName;
        //file is saved in C drive, in a new folder called text editor
        file = new File(Name);
        Scanner input = new Scanner(file);
        String s = "";
        while(input.hasNext())
        {
            s = s + input.nextLine() + "\n";
        }
        Text.setText(s);
    }

    public static void main(String[] args) {
        TextEditor main = new TextEditor();

        f.setLayout(new BorderLayout());
        f.setJMenuBar(MenuBar);
        f.getContentPane().add(jp);
        f.add(jp,BorderLayout.CENTER);
        f.add(Bottom,BorderLayout.SOUTH);

        f.setSize(800,600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}