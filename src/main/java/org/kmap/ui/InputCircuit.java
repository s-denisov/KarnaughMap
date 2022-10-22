package org.kmap.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.ui.inputcircuitsymbols.*;

import java.util.ArrayList;
import java.util.List;

public class InputCircuit {

    @FXML
    private AnchorPane drawing;
    @FXML
    private HBox starterGates;
    private List<GateOrIO> symbols = new ArrayList<>();
    private OutputSymbol output;
    private final GateOutput.ActiveChecker activeChecker = new GateOutput.ActiveChecker();

    private void createStarterGates() {
        String[] imageNames = new String[]{"and_gate.png", "or_gate.png", "not_gate.png"};
        for (String imageName : imageNames) {
            ImageView gate = new ImageView(new Image(getClass().getResource(imageName).toExternalForm()));
            starterGates.getChildren().add(gate);
            gate.setPickOnBounds(true);
            gate.setOnDragDetected(e -> {
                Dragboard db = gate.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(gate.getImage());
                content.putString(imageName);
                db.setContent(content);
            });
        }
    }

    @FXML
    private void initialize() {
        createStarterGates();
        drawing.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.COPY);
            e.consume();
        });
        drawing.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            ImageView im = new ImageView(db.getImage());
            im.setX(e.getX() - db.getImage().getWidth() / 2);
            im.setY(e.getY() - db.getImage().getHeight() / 2);
            GateOrIO gs = GateSymbol.create(db.getString(), im, e.getX(), e.getY(), this);
            symbols.add(gs);
            e.setDropCompleted(true);
            e.consume();
        });

        for (int i = 0; i < 4; i++) {
            symbols.add(new InputSymbol(drawing, i, activeChecker));
        }
        output = new OutputSymbol(drawing, this::setSelectedInput);
        symbols.add(output);
    }

    public void setSelectedInput(GateOrIO selectedOutput) {
        for (GateOrIO symbol : symbols) {
            if (symbol instanceof WithOutput) {
                ((WithOutput) symbol).handleOutputGate(selectedOutput);
            }
        }
    }

    public void deleteSymbol(GateOrIO symbol) {
        if (symbols.contains(symbol)) {
            drawing.getChildren().removeAll(symbol.getRepresentation());
            symbols.remove(symbol);
        }
        for (GateOrIO symbol2 : symbols) {
            if (symbol2 instanceof WithInput) ((WithInput) symbol2).removeInput(symbol);
        }
    }

    public void findLogic() {
        KarnaughGrid grid = (KarnaughGrid) App.setRoot("karnaugh-grid");
        grid.setInputCircuitSymbols(symbols);
        try {
            LogicElement logic = output.findLogic();
            grid.setInputExpressionText(logic.toString());
            grid.drawGridFromLogic(logic, 4);
        } catch (NullPointerException ignored) {
        }
    }

    public void setSymbols(List<GateOrIO> symbols) {
        this.symbols = symbols;
        drawing.getChildren().removeIf(x -> x.getClass() != HBox.class && x.getClass() != Button.class);
        for (GateOrIO symbol : symbols) {
            if (symbol instanceof OutputSymbol) output = (OutputSymbol) symbol;
            symbol.update(this);
            drawing.getChildren().addAll(symbol.getRepresentation());
        }
    }

    public AnchorPane getDrawing() {
        return drawing;
    }

    public GateOutput.ActiveChecker getActiveChecker() {
        return activeChecker;
    }
}
