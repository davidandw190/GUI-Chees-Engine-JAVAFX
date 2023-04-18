package com.main;


// For putting the pictures of the pieces onto the board
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// To store the "pieces" variable of all the pieces
import java.util.ArrayList;

// Imports to deal with the application, like drawing pictures and event handlers
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class App extends Application {

    // Using 3 groups, one for the pieces, one for the grid,
    // and one for the highlighted square
    // makes it easier to update the board with getChildren.clear();
    private static Scene scene;
    private static Group gridGroup;
    private static Group pieceGroup;
    private static Group highlightGroup;
    private static Pane pane;


    // For adding the pieces to the screen
    private static InputStream stream;
    private static Image image;
    private static ImageView imageView;


    // For storing the position of the mouse at 'click'
    private static int[] coordinates;


    // List of pieces to easier access, along with king declarations for setMoves()
    private static ArrayList<Piece> pieces = new ArrayList<Piece>();
    private static Piece whiteKing;
    private static Piece blackKing;
    private static Piece king;

    // Stores the current turn as "white" or "black"
    private static String turn;

    // Figures out if the curPiece is ready to be moved
    private static boolean toMove;
    private static Piece curPiece;

    // Placeholder Piece p to call isOccupied for this class
    private static Piece p = new Piece(-1, -1, "n/a", "placement", "filename");


    // File paths for all the pieces
    private static final String whiteRookFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whiteRook.png";
    private static final String blackRookFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackRook.png";
    private static final String whiteKnightFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whiteKnight.png";
    private static final String blackKnightFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackKnight.png";
    private static final String whiteBishopFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whiteBishop.png";
    private static final String blackBishopFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackBishop.png";
    private static final String whiteQueenFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whiteQueen.png";
    private static final String blackQueenFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackQueen.png";
    private static final String whiteKingFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whiteKing.png";
    private static final String blackKingFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackKing.png";
    private static final String whitePawnFileStr = "src\\main\\resources\\com\\main\\PiecePics\\whitePawn.png";
    private static final String blackPawnFileStr = "src\\main\\resources\\com\\main\\PiecePics\\blackPawn.png";

    @Override
    public void start(Stage stage) throws IOException {

        // Assigns the groups and pane
        gridGroup = new Group();
        pieceGroup = new Group();
        highlightGroup = new Group();
        pane = new Pane(gridGroup, pieceGroup, highlightGroup);

        // Constructs the actual board
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                // Makes the grid with alternating colors white and blue
                Rectangle rect = new Rectangle();

                coordinates = coordinateFormula(i, j); // To find the top left corner of the triangle

                rect.setX(coordinates[0]);
                rect.setY(coordinates[1]);
                rect.setWidth(100);
                rect.setHeight(100);

                if ( count % 2 == 0) { rect.setFill(Color.WHITE); }
                else { rect.setFill(Color.rgb(51, 153, 175)); }

                gridGroup.getChildren().add(rect);

                count++;
            }

            count++;
        }

        setUpPieces();
        drawBoard();

        // Declares the scene with after the pieces are added to the screen
        scene = new Scene(pane, 850, 850);

        // A scene of 850x850 fit the pieces onto the screen perfectly, not sure how to resize the images
        stage.setScene(scene);
        stage.setTitle("David`s Chess Game");
        stage.setResizable(false);
        stage.show();

        startGame();

    }

    private void setUpPieces() {

        /*
         * Sets up all the pieces with coordinates,
         * color, and set the available moves
         * also set the whiteKing and blackKing accordingly
         */

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = null;

                if (j == 0) { // 8th rank
                    if (i == 0 || i == 7) {piece = new Piece(i, j, "black", "rook", blackRookFileStr);} // add black rook
                    if (i == 1 || i == 6) {piece = new Piece(i, j, "black", "knight", blackKnightFileStr);} // add black knight
                    if (i == 2 || i == 5) {piece = new Piece(i, j, "black", "bishop", blackBishopFileStr);} // add black bishop
                    if (i == 3) {piece = new Piece(i, j, "black", "queen", blackQueenFileStr);} // add black queen

                    if (i == 4) { // Adds black king
                        piece = new Piece(i, j, "black", "king", blackKingFileStr);
                        blackKing = piece;
                    }
                }

                if (j == 1) {piece = new Piece(i, j, "black", "pawn", blackPawnFileStr);} // add black pawns
                if (j == 6) {piece = new Piece(i, j, "white", "pawn", whitePawnFileStr);} // add white pawns

                if (j == 7) { // 1st rank
                    if (i == 0 || i == 7) {piece = new Piece(i, j, "white", "rook", whiteRookFileStr);} // add white rook
                    if (i == 1 || i == 6) {piece = new Piece(i, j, "white", "knight", whiteKnightFileStr);} // add white knight
                    if (i == 2 || i == 5) {piece = new Piece(i, j, "white", "bishop", whiteBishopFileStr);} // add white bishop
                    if (i == 3) {piece = new Piece(i, j, "white", "queen", whiteQueenFileStr);} // add white queen
                    if (i == 4) { // Adds white king
                        piece = new Piece(i, j, "white", "king", whiteKingFileStr);
                        whiteKing = piece;
                    }
                }
                if (piece != null) {pieces.add(piece);}
            }
        }
    }


    private void highlightSquare (int x, int y) {

        /*
         * Highlights a chosen square so long as
         * the click is inside the grid, helps the user
         * to determine where they have clicked and if they have
         * selected a piece or not
         */

        highlightGroup.getChildren().clear();
        Rectangle rect = new Rectangle();
        coordinates = coordinateFormula(x, y);
        rect.setX(coordinates[0]);
        rect.setY(coordinates[1]);
        rect.setWidth(100);
        rect.setHeight(100);
        rect.setFill(Color.rgb(0, 255, 0));
        rect.setOpacity(0.15);

        highlightGroup.getChildren().add(rect);
    }

    private void startGame() {
        // Assigns the first turn to the table

        turn = "white";

        var mouseEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // Getting x and y coordinates
                int x = (int)event.getX();
                int y = (int)event.getY();
                int[] square = findSquare(x, y); // finds the 0-indexed row and column of where the user clicked
                x = square[0];
                y = square[1];

                highlightSquare(x, y); // highlight the chosen square

                // Sets the king for the setMoves methods
                king = turn.equals("white") ? whiteKing: blackKing;

                if (!toMove) {
                    toMove = setPiece(x, y, toMove); // satisfying one liner
                }
                else {
                    for (Location location: curPiece.getMoves()) {
                        if (location.getX() == x && location.getY() == y) {

                            // Removes the piece in case it is taken
                            Piece occupiedPiece = p.isOccupied(pieces, new Location(x, y));
                            if (occupiedPiece != null) {pieces.remove(occupiedPiece);}

                            String type = curPiece.type; // this helps to set for castling evaluation
                            if (type.equals("rook") || type.equals("king")) {curPiece.hasMoved = true;}

                            // Calls castle to make this shorter
                            checkCastled(x);

                            // Moves the piece to its new location
                            curPiece.setX(x);
                            curPiece.setY(y);

                            checkPromotion(y);

                            // clear the groups, prepare for redraw
                            pieceGroup.getChildren().clear();
                            highlightGroup.getChildren().clear();

                            // if turn is white, then turn is black and vice versa
                            turn = turn.equals("white") ? "black": "white";
                            king = turn.equals("white") ? whiteKing: blackKing;

                            king.check = king.inCheck(pieces, king); // determine if the king is in check

                            checkConditions(pieces, turn); // this looks for stalemate or checkmate

                            flipBoard(); // makes the game much cooler when flipping the board

                            try {
                                drawBoard();
                            } catch (FileNotFoundException e) {e.printStackTrace(); System.out.println("paths are likely wrong");}
                        }
                        toMove = setPiece(x, y, toMove); // see if user selected another piece of same color to move
                    }

                }

            }

        };


        scene.setOnMouseClicked(mouseEventHandler);
    }

    private void checkCastled(int x) {

        /*
         * this function checks to see if the king has castled,
         * and if so, where the rook should move because of the
         * distinction between king and queen side
         * also, this is made a lot easier when the board is flipped
         * so the side playing is always on the bottom
         */

        // If the king castled, moves the rook accordingly
        if (curPiece.getType().equals("king")) {
            Piece rookToTransfer;
            if (curPiece.x - x == -2) { // right side castle
                rookToTransfer = curPiece.isOccupied(pieces, new Location(7, 7));
                rookToTransfer.x = x - 1;
            }
            if (curPiece.x - x == 2) { // left side castle
                rookToTransfer = curPiece.isOccupied(pieces, new Location(0, 7));
                rookToTransfer.x = x + 1;
            }
        }

    }


    private void checkPromotion(int y) {

        /*
         * Checks if the pawn has reached the promotion square
         * although either a knight or queen should be minimally allowed,
         * I decided to decrease that work and just make it a queen, so
         * hopefully the user likes queens
         *
         * An easy way to decide would be to show a prompt label asking for
         * a queen or knight, simply by saying
         *
         * Press "1" for a queen, and "2" for a knight
         *
         * Note: This is a way easier when flipping the board
         */

        if (curPiece.type.equals("pawn") && curPiece.y == 0) {
            curPiece.type = "queen";
            if (curPiece.color.equals("white")) {curPiece.setFileString(whiteQueenFileStr);}
            else {curPiece.setFileString(blackQueenFileStr);}
        }


    }

    private void checkConditions(ArrayList<Piece> pieces, String turn) {

        /*
         * After every move, needs to evaluate whether the
         * enemy's king is checkmated or it is a stalemate
         */

        if (king.checkMate(pieces, turn)) {
            String otherColor = turn.equals("white") ? "black": "white";
            System.out.println("checkmate for " + otherColor);
        }
        if (king.staleMate(pieces, turn)) {
            System.out.println("stalemate");


        }
    }

    private boolean setPiece(int x, int y, boolean toMove) {

        /*
         * Using the placeholder Piece p, find whether
         * the user has selected a piece that matches their color,
         * so that on the next clicked it can be moved
         */

        try {
            Piece piece = p.isOccupied(pieces, new Location(x, y));
            if (piece.color.equals(turn)) {
                curPiece = piece;
                curPiece.setMoves(pieces, curPiece, king, true);
                return true;
            }
        } catch (NullPointerException e) {}
        return false;

    }

    private void flipBoard() {

        /*
         * Flips the board, so that the current turn
         * of the game is at the bottom of the screen,
         * makes for easier playing
         *
         * For flipping the pieces, the pieces need
         * to be flipped on a x-axis and a y-axis, which
         * I centered at coulmn and row 4
         */

        for (Piece piece: pieces) {
            int xDifference = Math.abs(3 - piece.x);
            if (piece.x <= 3) {piece.x = 4 + xDifference;}
            else {piece.x = 4 - xDifference;}
            int yDifference = Math.abs(3 - piece.y);
            if (piece.y <= 3) {piece.y = 4 + yDifference;}
            else {piece.y = 4 - yDifference;}
        }
    }

    private int[] findSquare(int x, int y) {

        /*
         * Returns the square coordinates
         * given the clicked mouse coords
         *
         * Each sqaure is 100 units wide and
         * 100 unis long
         *
         * we want to use floor division here
         */

        int[] res = new int[2];
        res[0] = (x-25)/100;
        res[1] = (y-25)/100;
        return res;
    }

    private void drawBoard() throws FileNotFoundException {

        /*
         * Using all the Piece objects active in pieces,
         * draw them to the screen given their corresponding
         * coordinates
         */

        for (Piece piece: pieces) {
            setImage(piece.getFileString(), piece.getX(), piece.getY());
        }
    }

    private void setImage(String path, int i, int j)  throws FileNotFoundException {

        /*
         * Delivers the pictures of the pieces
         * with a String pathname to the board,
         * by calling coordinateFormula() and using
         * pieceGroup
         */

        try {
            stream = new FileInputStream(path);
            image = new Image(stream);
            imageView = new ImageView();
            imageView.setImage(image);
            coordinates = coordinateFormula(i, j);
            imageView.setX(coordinates[0]);
            imageView.setY(coordinates[1]);
            pieceGroup.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("cannot find file");
            e.printStackTrace();}
    }

    private int[] coordinateFormula(int x, int y) {

        /*
         * Given an x and y coordinate,
         * finds the corresponding location
         * for the top left part of an image
         * and rectangle
         */

        int[] result = new int[2];
        result[0] = 25 + 100 * x;
        result[1] = 25 + 100 * y;
        return result;

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();

    }

    public static void main(String[] args) {
        launch();
    }



}


