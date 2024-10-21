import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Window extends JFrame{

    boolean isMousePointerExited;

    public JPanel drawPanel;
    private JPanel settingPanel;

    public static int moveableLeftX;
    public static int moveableRightX;

    public static int windowWidth;
    public static int windowHeight;

    // 画面サイズ÷実際のPCのウィンドウサイズ
    public static double ratioWidthOfRealWindowSize;
    public static double ratioHeightOfRealWindowSize;

    private JCheckBox isLineMode;
    private JSlider lineCounter;
    private JButton[] settingButtons;

    private int page;

    public boolean[] hasWall = {false,false,false,false,false,false,false,};

    static{
        windowWidth = 1600;
        windowHeight = 900;

        if (windowWidth / windowHeight != 16/9) throw new IllegalArgumentException("設定したWindowの幅と高さの比が16:9になっていません。");

        moveableLeftX  = windowWidth / 4;
        moveableRightX = windowWidth - moveableLeftX; 

        java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        java.awt.DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
        // 変数widthとheightに画面の解像度の幅と高さを代入
        ratioWidthOfRealWindowSize  = (double)windowWidth  / displayMode.getWidth();
        ratioHeightOfRealWindowSize = (double)windowHeight / displayMode.getHeight();
    }

    Window(){
        this.setTitle("stage maker");
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.isMousePointerExited = true;
        this.page = 0;

        this.drawPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
       this.drawPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()){
                    //左クリック
                    case MouseEvent.BUTTON1:
                        if(App.nowEntity==EntityKind.NONE || e.getX() < moveableLeftX || e.getX() > moveableRightX )return;
                        
                        int formattedX = e.getX() - e.getX()%10;
                        int formattedY = e.getY() - e.getY()%10; 
                        App.entities.add(new Entity(formattedX, formattedY, App.nowEntity,page));
                        if (isLineMode.isSelected()){
                            for (int i = 1; i < lineCounter.getValue() ; i++){
                                int formattedLeftX  = formattedX - 20 * i;
                                if (formattedLeftX > moveableLeftX) App.entities.add(new Entity(formattedLeftX, formattedY, App.nowEntity,page));
                                int formattedRightX = formattedX + 20 * i;
                                if (formattedRightX < moveableRightX) App.entities.add(new Entity(formattedRightX, formattedY, App.nowEntity,page));
                            }
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        App.nowEntity = 0;
                        break;
                    //右クリック 
                    case MouseEvent.BUTTON3:
                        for (int i = App.entities.size() - 1; i >= 0 ; i--) {
                            Entity entity = App.entities.get(i);
                            if(entity.isPointed(e.getX(), e.getY())){
                                App.entities.remove(entity);
                            }
                        }
                        break;
                    case 4:
                        hasWall[page] = !hasWall[page];
                        break;
                    default:
                        if(App.nowEntity < EntityKind.SPEED_UP_ITEM){
                            App.nowEntity++;
                            App.tempImage = EntityImages.getImage(App.nowEntity);
                        }else{
                            App.nowEntity = EntityKind.BIG_ENEMY;
                        }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isMousePointerExited = false;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isMousePointerExited = true;
            }
            @Override
            public void mousePressed(MouseEvent e) {
                
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
        });

        this.addMouseWheelListener(e -> {
            //手前に回す
            if(e.getWheelRotation() == 1){
                if(App.nowEntity > 0){
                    App.nowEntity--;
                }else{
                    App.nowEntity = EntityKind.SPEED_UP_ITEM;
                }
                App.tempImage = EntityImages.getImage(App.nowEntity);
            }else if(e.getWheelRotation() == -1){
                if(App.nowEntity < EntityKind.SPEED_UP_ITEM){
                    App.nowEntity++;
                }else{
                    App.nowEntity = EntityKind.BIG_ENEMY;
                }
                App.tempImage = EntityImages.getImage(App.nowEntity);
            }
        });

        this.settingPanel = new JPanel();
        this.settingPanel.setBackground(Color.CYAN);

        this.isLineMode = new JCheckBox("LINE:");
        this.isLineMode.setBackground(Color.CYAN);
        this.settingPanel.add(isLineMode);

        this.lineCounter = new JSlider(2, 10, 2);
        this.lineCounter.setLabelTable(lineCounter.createStandardLabels(1));
        this.lineCounter.setPaintLabels(true);
        this.lineCounter.setBackground(Color.CYAN);
        this.settingPanel.add(lineCounter);
        
        {
            JButton[] tmp_buttons = {
                new JButton("SAVE"),
                new JButton("LOAD"),
                new JButton("BACK"),
                new JButton("NEXT"),
                new JButton("CLEAR"),
            };
            this.settingButtons = tmp_buttons;
        }

        this.settingButtons[0].setBackground(Color.RED);
        this.settingButtons[0].setForeground(Color.WHITE);
        this.settingButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File old = new File("data.txt");
                old.delete();
                File newData = new File("data.txt");

                try{
                    newData.createNewFile();
                }catch(IOException exception){
                    exception.printStackTrace();
                }
                try (Writer saveWriter = new BufferedWriter(new FileWriter(newData));) {
                    //save処理

                    for (int i = 0; i < hasWall.length; i++) {
                        if(hasWall[i]){
                            saveWriter.write("-1,"+i+",-1,-1,");
                            saveWriter.write("\n");
                        }
                    }

                    //stage_position(int) , 0 / 1 , enemy_type(EnemyType) / item_type(ItemType) , x (int)  \n
                    List<Entity> saveDatas = new ArrayList<Entity>(App.entities.size());
                    for (Entity entity : App.entities) {
                        saveDatas.add(new Entity(entity));
                    }
                    for (Entity entity : saveDatas) {
                        entity.y = (windowHeight - entity.y) + windowHeight * entity.page;
                    }
                    saveDatas.sort((en1, en2) -> {
                        if (en1.y > en2.y)return 1;
                        if (en1.y < en2.y)return -1;
                        return 0;
                    });
                    for (int i=0;i<saveDatas.size();i++) {
                        Entity entity = saveDatas.get(i);

                        int isEnemy = 0;
                        if(entity.kind >= EntityKind.CLEAR_ENEMIES_ITEM){
                            entity.kind -= EntityKind.CLEAR_ENEMIES_ITEM;
                            isEnemy = 1;
                        }
                        saveWriter.write(entity.y+","+isEnemy+","+entity.kind+","+entity.x+",");
                        if(i<saveDatas.size()-1)saveWriter.write("\n");
                    }
                    saveWriter.flush();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        this.settingButtons[1].setBackground(Color.RED);
        this.settingButtons[1].setForeground(Color.WHITE);
        this.settingButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Scanner sc = new Scanner(new File("data.txt"))) {
                    //load処理
                    //stage_position(int) , 0 / 1 , enemy_type(EnemyType) / item_type(ItemType) , x (int)  \n

                    App.entities.clear();

                    sc.useDelimiter(",");

                    while(sc.hasNextLine()){
                        //次の行を読み込み
                        int stagePos = sc.nextInt();
                        
                        if (stagePos == -1){
                            int index = sc.nextInt();
                            hasWall[index] = true;
                            sc.nextLine();
                            continue;
                        }

                        int flag = sc.nextInt();
                        int kind = sc.nextInt() + (flag == 1 ? EntityKind.CLEAR_ENEMIES_ITEM : 0);
                        int x = sc.nextInt();
                        int realY = stagePos % windowHeight;
                        int y = windowHeight - realY;
                        int page = (stagePos-realY) / windowHeight;
                        App.entities.add(new Entity(x, y, kind, page));

                        sc.nextLine();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        this.settingButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(page > 0)page--;
            }
        });
        this.settingButtons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(page < 7)page++;
            }
        });

        this.settingButtons[4].setBackground(Color.YELLOW);
        this.settingButtons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = App.entities.size() - 1; i >= 0; i--){
                    if(App.entities.get(i).page == page){
                        App.entities.remove(i);
                    }
                }
            }
        });

        this.add(this.drawPanel,BorderLayout.CENTER);
        this.add(this.settingPanel,BorderLayout.SOUTH);

        for (JButton jButton : this.settingButtons) {
            this.settingPanel.add(jButton);
        }
    }

    private void draw(Graphics g){

        System.out.println(Arrays.toString(hasWall));

        if(hasWall[page]){
            g.setColor(Color.ORANGE);

            g.fillRect(moveableLeftX, 0, 50, windowHeight);
            g.fillRect(moveableRightX - 50, 0,50, windowHeight);

            g.setColor(Color.BLACK);
        }

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, moveableLeftX, windowHeight);
        g.fillRect(moveableRightX, 0, windowWidth - moveableRightX, windowHeight);
        
        g.setColor(Color.black);
        for (int i = 0;i<windowHeight;i+=10){
            g.drawLine(0, i, windowWidth, i);
            for (int j = 0; j < windowWidth; j+=10) {
                g.drawLine(j, 0, j, windowHeight);
            }
            g.drawString("y="+i, windowWidth - 50, i);
        }

        if (App.nowEntity != EntityKind.NONE){
            if (App.nowEntity == EntityKind.CYCLONE_ENEMY){
                g.drawImage(App.tempImage, (int)App.mousePoint.getX(), (int)App.mousePoint.getY(), 32, 32, null);
            }else{
                g.drawImage(App.tempImage, (int)App.mousePoint.getX(), (int)App.mousePoint.getY(), null);
            }          
            g.setColor(Color.BLUE);
            g.fillOval((int)App.mousePoint.getX() - 5, (int)App.mousePoint.getY() -5 , 10, 10);
            g.setColor(Color.BLACK);
        }

        for (Entity entity : App.entities) {
            if (entity.page == this.page){
                g.drawImage(EntityImages.getImage(entity.kind),entity.x,entity.y, (int)(entity.r*2 * ratioWidthOfRealWindowSize), (int)(entity.r*2 * ratioHeightOfRealWindowSize),null);
            }
        }

        g.setColor(Color.CYAN);
        g.fillRect(10, 0, 50,30);

        g.setColor(Color.red);
        g.drawString("page:" + page, 10, 10);
        g.drawString(App.mousePoint.x+":"+App.mousePoint.y , 10,20);
        g.drawString(App.nowEntity + "",10,30);
    }


}
