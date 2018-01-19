package pistalix.dslrcamera.dslrcamerahd;

/**
 * Created by xyz on 1/11/2017.
 */
public class Modal {

    int thumbId, FrmId;

    public Modal(int thumbId, int frmId) {
        this.thumbId = thumbId;
        FrmId = frmId;
    }

    public int getThumbId() {
        return thumbId;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public int getFrmId() {
        return FrmId;
    }

    public void setFrmId(int frmId) {
        FrmId = frmId;
    }
}
