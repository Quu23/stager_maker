
public class Entity {
    int x;
    int y;
    int kind;
    int page;

    int r;

    Entity(Entity e){
        this.x = e.x;
        this.y = e.y;
        this.kind = e.kind;
        this.page = e.page;
        this.r = e.r;
    }

    Entity(int x,int y,int kind,int page){

        this.x = x;
        this.y = y;
        this.kind = kind;
        this.page = page;

        switch(kind){
            case EntityKind.BIG_ENEMY:
                r = 45;
                break;
            case EntityKind.GOLDEN_ENEMY: 
                r = 10;
                break;
            case EntityKind.HEXAGON_ENEMY:
                r = 20;
                break;
            case EntityKind.MISSILE_ENEMY:
                r = 20;
                break;
            case EntityKind.SHOTGUN_ENEMY:
                r = 20;
                break;
            case EntityKind.SNAKE_ENEMY:
                r = 20;
                break;
            case EntityKind.SPLASH_ENEMY:
                r = 20;
                break;
            case EntityKind.SPLIT_ENEMY:
                r = 40;
                break;
            case EntityKind.STRAIGHT_ENEMY:
                r = 20;
                break;
            case EntityKind.TURNBACK_ENEMY:
                r = 20;
                break;

            case EntityKind.CLEAR_ENEMIES_ITEM:
                r = 16;
                break;
            case EntityKind.DESTROY_ITEM:
                r = 16;
                break;
            case EntityKind.EXP_ORB:
                r = 8;
                break;
            case EntityKind.HEALING_ITEM:
                r = 16;
                break;
            case EntityKind.INVINCIBLE_ITEM:
                r = 16;
                break;
            case EntityKind.SCORE_BOOSTER_ITEM:
                r = 16;
                break;
            case EntityKind.SHOT_RATE_DOWN_ITEM:
                r = 16;
                break;
            case EntityKind.SHOT_RATE_UP_ITEM:
                r = 16;
                break;
            case EntityKind.SPEED_DOWN_ITEM:
                r = 16;
                break;
            case EntityKind.SPEED_UP_ITEM:
                r = 16;
                break;
        }
    }

    public boolean isPointed(int x, int y) {
        int dx = this.x + r - x;
        int dy = this.y + r - y;

        if (dx * dx + dy * dy < r * r)return true;
        return false;
    }

}
