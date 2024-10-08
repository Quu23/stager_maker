import java.awt.Image;
import java.awt.Toolkit;

public class EntityImages {
    public static Image getImage(int kind){
        String path;

        switch(kind){
            case EntityKind.BIG_ENEMY:
                path = "img/Big_Enemy.png";
                break;
            case EntityKind.GOLDEN_ENEMY: 
                path = "img/Golden_Enemy.png";
                break;
            case EntityKind.HEXAGON_ENEMY:
                path = "img/Hexagon_Enemy.png";
                break;
            case EntityKind.MISSILE_ENEMY:
                path = "img/Missile_Enemy.png";
                break;
            case EntityKind.SHOTGUN_ENEMY:
                path = "img/ShotgunEnemy.png";
                break;
            case EntityKind.SNAKE_ENEMY:
                path = "img/SnakeEnemy.png";
                break;
            case EntityKind.SPLASH_ENEMY:
                path = "img/Splash_Enemy.png";
                break;
            case EntityKind.SPLIT_ENEMY:
                path = "img/Split_Enemy.png";
                break;
            case EntityKind.STRAIGHT_ENEMY:
                path = "img/StraightEnemy.png";
                break;
            case EntityKind.TURNBACK_ENEMY:
                path = "img/Trunback_Enemy.png";
                break;

            case EntityKind.CLEAR_ENEMIES_ITEM:
                path = "img/Clear_Enemies_Item.png";
                break;
            case EntityKind.DESTROY_ITEM:
                path = "img/Destroy_Item.png";
                break;
            case EntityKind.EXP_ORB:
                path = "img/Exp_Orb.png";
                break;
            case EntityKind.HEALING_ITEM:
                path = "img/Healing_Item.png";
                break;
            case EntityKind.INVINCIBLE_ITEM:
                path = "img/Invincible_Item.png";
                break;
            case EntityKind.SCORE_BOOSTER_ITEM:
                path = "img/Score_Booster_Item.png";
                break;
            case EntityKind.SHOT_RATE_DOWN_ITEM:
                path = "img/Shot_Rate_Down_Item.png";
                break;
            case EntityKind.SHOT_RATE_UP_ITEM:
                path = "img/Shot_Rate_Up_Item.png";
                break;
            case EntityKind.SPEED_DOWN_ITEM:
                path = "img/Speed_Down_Item.png";
                break;
            case EntityKind.SPEED_UP_ITEM:
                path = "img/Speed_Up_Item.png";
                break;
            default : 
                path = "";
                throw new IllegalArgumentException("アイテムを指定してください。");
        }

        return Toolkit.getDefaultToolkit().getImage(path);

    }
}
