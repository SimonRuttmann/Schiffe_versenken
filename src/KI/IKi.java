package KI;

import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.IPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

import java.util.ArrayList;

public interface IKi {
    ShotResponse getShot(IOwnPlayground playground);

    ArrayList<IShip> placeships(IOwnPlayground playground);
}
