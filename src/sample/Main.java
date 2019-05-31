package sample;

import javax.swing.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.GroupLayout.*;


public class Main extends JFrame
        implements DocumentListener {

    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTextArea textArea;

    private static final String COMMIT_ACTION = "commit";
    private static enum Mode { INSERT, COMPLETION };
    private final List<String> words;
    private Mode mode = Mode.INSERT;



    public Main() {
        super("TextAreaDemo");
        initComponents();

        textArea.getDocument().addDocumentListener(this);

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());

        words = new ArrayList<String>(5);
        words.add("alias");
        words.add("array");
        words.add("and");
        words.add("backtrace");
        words.add("begin");
        words.add("break");
        words.add("byte");
        words.add("case");
        words.add("catch");
        words.add("chdir");
        words.add("chmod");
        words.add("class");
        words.add("close");
        words.add("clear");
        words.add("collect");
        words.add("collection");
        words.add("def");
        words.add("default");
        words.add("defined");
        words.add("delete");
        words.add("directory");
        words.add("do");
        words.add("each");
        words.add("else");
        words.add("elsif");
        words.add("empty");
        words.add("end");
        words.add("ensure");
        words.add("entries");
        words.add("executable");
        words.add("equal");
        words.add("false");
        words.add("fetch");
        words.add("file");
        words.add("for");
        words.add("foreach");
        words.add("ftype");
        words.add("function");
        words.add("gets");
        words.add("hash");
        words.add("if");
        words.add("in");
        words.add("include");
        words.add("index");
        words.add("indices");
        words.add("inspect");
        words.add("invert");
        words.add("keys");
        words.add("length");
        words.add("local");
        words.add("merge");
        words.add("message");
        words.add("mkdir");
        words.add("module");
        words.add("new");
        words.add("next");
        words.add("nil");
        words.add("not");
        words.add("open");
        words.add("or");
        words.add("pack");
        words.add("path");
        words.add("print");
        words.add("puts");
        words.add("pwd");
        words.add("raise");
        words.add("readable");
        words.add("readlines");
        words.add("redo");
        words.add("rehash");
        words.add("reject");
        words.add("require");
        words.add("rescue");
        words.add("retry");
        words.add("return");
        words.add("self");
        words.add("shift");
        words.add("size");
        words.add("sort");
        words.add("store");
        words.add("string");
        words.add("super");
        words.add("syswrite");
        words.add("then");
        words.add("throw");
        words.add("time");
        words.add("to_s");
        words.add("true");
        words.add("undef");
        words.add("unless");
        words.add("until");
        words.add("update");
        words.add("utc");
        words.add("value");
        words.add("variable");
        words.add("when");
        words.add("while");
        words.add("writeable");
        words.add("yield");
        words.add("zero");
        /// so ok



    }


    private void initComponents() {
        jLabel1 = new JLabel("Try typing 'spectacular' or 'Swing'...");

        textArea = new JTextArea();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);

        jScrollPane1 = new JScrollPane(textArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        SequentialGroup h1 = layout.createSequentialGroup();
        ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);
        h2.addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE);


        h1.addContainerGap();
        h1.addGroup(h2);
        h1.addContainerGap();
        hGroup.addGroup(Alignment.TRAILING,h1);
        layout.setHorizontalGroup(hGroup);

        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup v1 = layout.createSequentialGroup();
        v1.addContainerGap();
        v1.addComponent(jLabel1);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE);
        v1.addContainerGap();
        vGroup.addGroup(v1);
        layout.setVerticalGroup(vGroup);
        pack();

    }

    public void changedUpdate(DocumentEvent ev) {
    }

    public void removeUpdate(DocumentEvent ev) {
    }

    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1) {
            return;
        }

        int pos = ev.getOffset();
        String content = null;
        try {
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        int w;
        for (w = pos; w >= 0; w--) {
            if (! Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 2) {
            return;
        }

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(words, prefix);
        if (n < 0 && -n <= words.size()) {
            String match = words.get(-n - 1);
            if (match.startsWith(prefix)) {
                String completion = match.substring(pos - w);

                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1));
            }
        } else {

            mode = Mode.INSERT;
        }
    }

    private class CompletionTask implements Runnable {
        String completion;
        int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            textArea.insert(completion, position);
            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

    private class CommitAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textArea.getSelectionEnd();
                textArea.insert(" ", pos);
                textArea.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textArea.replaceSelection("\n");
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new Main().setVisible(true);
            }
        });
    }


}