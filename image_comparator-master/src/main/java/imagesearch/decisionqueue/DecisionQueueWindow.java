package imagesearch.decisionqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.webkit.UIClientImpl;
import imagesearch.CustomBufferedImage;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DecisionQueueWindow extends JFrame{
    private final List<Pair<String, String>> possibleClones;
    private final String outputFileName;
    private final List<String> toBeDeleted;
    private Pair<UiImage, UiImage> comparedImages;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private int listIndex = -1;


    public DecisionQueueWindow(List<Pair<String, String>> possibleClones, String outputFileName) {
        this.possibleClones = possibleClones;
        this.outputFileName = outputFileName;
        this.comparedImages = Pair.of(UiImage.dummy(), UiImage.dummy());
        this.toBeDeleted = new ArrayList<>();
        this.init();
    }

    private void init() {
        this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(1500, 880));
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(comparedImages.getLeft() == null ? UiImage.dummy() : comparedImages.getLeft(), BorderLayout.WEST);
        imagePanel.add(comparedImages.getRight() == null ? UiImage.dummy() : comparedImages.getRight(), BorderLayout.EAST);
        this.add(imagePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(1500, 40));
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(new DeleteButton());
        buttonPanel.add(new PrevInQueue(this));
        buttonPanel.add(new NextInQueue(this));
        buttonPanel.add(new SaveQueueButton());
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    //    private UiImage testImage() {
//        try {
//            return new UiImage(ImageIO.read(new File("C:\\Users\\Alex\\Desktop\\fav\\todelete\\z_18422866_285755665216710_5585575423684617779_o.jpg")),
//                    "C:\\Users\\Alex\\Desktop\\fav\\todelete\\z_18422866_285755665216710_5585575423684617779_o.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    private class DeleteButton extends JButton{
        DeleteButton(){
            super("Delete");
            this.addActionListener(e -> toBeDeleted.add(comparedImages.getLeft().getFileName()));
        }
    }

    private class SaveQueueButton extends JButton{
        public SaveQueueButton() {
            super("Save progress");

            this.addActionListener(e -> {
                try {
                    FileWriter fos = new FileWriter(new File(outputFileName));
                    BufferedWriter oos = new BufferedWriter(fos);
                    oos.write(objectMapper.writer().writeValueAsString(toBeDeleted));
                    oos.close();
                    System.out.println("Saved progress");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private class NextInQueue extends JButton{
        private DecisionQueueWindow parent;

        public NextInQueue(DecisionQueueWindow parent) {
            super("Next");
            this.parent = parent;

            this.addActionListener(e -> {
                e.getSource();
                listIndex++;
                String leftName = possibleClones.get(listIndex).getLeft();
                String rightName = possibleClones.get(listIndex).getRight();
                System.out.println("Current listIndex: " + listIndex);

                while(listIndex < possibleClones.size() && leftName.equals(rightName)){
                    listIndex++;
                    leftName = possibleClones.get(listIndex).getLeft();
                    rightName = possibleClones.get(listIndex).getRight();
                    System.out.println("Incremented listIndex to " + listIndex);
                }

                try {
                    comparedImages.getLeft().updateImage(leftName);
                    comparedImages.getRight().updateImage(rightName);

                    parent.repaint();
                    parent.setTitle(leftName + " | " + rightName);
                } catch(Exception ex){
                    System.out.println(leftName);
                    System.out.println(rightName);
                    ex.printStackTrace();
                }
            });
        }
    }

    private class PrevInQueue extends JButton{
        private DecisionQueueWindow parent;

        public PrevInQueue(DecisionQueueWindow parent) {
            super("Prev");
            this.parent = parent;

            this.addActionListener(e -> {
                listIndex--;
                String leftName = possibleClones.get(listIndex).getLeft();
                String rightName = possibleClones.get(listIndex).getRight();
                System.out.println("Current listIndex: " + listIndex);

                while(listIndex >=0 && leftName.equals(rightName)){
                    listIndex--;
                    leftName = possibleClones.get(listIndex).getLeft();
                    rightName = possibleClones.get(listIndex).getRight();
                    System.out.println("Decremented listIndex to " + listIndex);
                }

                try {
                    comparedImages.getLeft().updateImage(leftName);
                    comparedImages.getRight().updateImage(rightName);
                    parent.repaint();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            });
        }
    }
}
