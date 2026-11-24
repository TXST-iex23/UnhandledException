package gui;

import common.BookingManager;

/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class GraphicUI{
    /** The application inventory that this menu operates on. */
    private BookingManager manager;


    /**
     * Initiates the graphical window of the application    */
    public GraphicUI( ){
        this.manager = new BookingManager();
    }

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public GraphicUI(BookingManager manager){
        this.manager = manager;
    }

//-----------------------------------------------------------------

    public void run( ){

    }

}
