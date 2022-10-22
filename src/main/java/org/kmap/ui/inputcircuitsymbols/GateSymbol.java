package org.kmap.ui.inputcircuitsymbols;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.kmap.logic.logicelements.LogicElement;
import org.kmap.ui.InputCircuit;

import java.util.ArrayList;
import java.util.List;

public abstract class GateSymbol implements WithInput, WithOutput {
    protected double centerX;
    protected double centerY;
    protected List<Node> representation = new ArrayList<>();
    protected Pane root;
    protected List<GateOrIO> inputs = new ArrayList<>();
    protected GateOutput outputCircle;
    protected InputCircuit parent;

    public GateSymbol(ImageView imageView, double centerX, double centerY, InputCircuit _parent) {
        this.centerX = centerX;
        this.centerY = centerY;
        parent = _parent;
        this.root = parent.getDrawing();
        root.getChildren().add(imageView);
        ColorAdjust redden = new ColorAdjust();
        redden.setHue(0);
        redden.setSaturation(10);
        imageView.setOnMouseEntered(e -> {
            if (parent.getActiveChecker().noneActive()) imageView.setEffect(redden);
        });
        imageView.setOnMouseExited(e -> imageView.setEffect(null));
        imageView.setOnMousePressed(e -> {
            if (parent.getActiveChecker().noneActive()) {
                parent.deleteSymbol(this);
                for (GateOrIO input : inputs) {
                    if (input instanceof WithOutput) ((WithOutput) input).resetOutputLines();
                }
            }
        });
        representation.add(imageView);
        for (double inputY : getInputsY()) {
            Circle inputCircle = GateOutput.createIOCircle(getInputsX(), inputY);
            inputCircle.setOnMousePressed(e -> parent.setSelectedInput(this));
            root.getChildren().add(inputCircle);
            representation.add(inputCircle);
        }
        outputCircle = new GateOutput(root, representation, getOutputX(), getOutputY(), parent.getActiveChecker());
    }

    public static GateOrIO create(String imageName, ImageView imageView, double centerX, double centerY, InputCircuit parent) {
        if (imageName.contains("and")) return new AndGate(imageView, centerX, centerY, parent);
        if (imageName.contains("or")) return new OrGate(imageView, centerX, centerY, parent);
        if (imageName.contains("not")) return new NotGate(imageView, centerX, centerY, parent);
        throw new IllegalArgumentException("Image name doesn't correspond to a logic gate");
    }

    public abstract double getInputsX();

    public abstract double[] getInputsY();

    public abstract double getOutputX();

    public double getOutputY() {
        return centerY + 0.5;
    }

    @Override
    public List<Node> getRepresentation() {
        return representation;
    }

    @Override
    public void handleOutputGate(GateOrIO outputGate) {
        if (outputGate instanceof WithInput && outputCircle.finishLine()) {
            ((WithInput) outputGate).addInput(this);
        }
    }

    @Override
    public void addInput(GateOrIO inputSymbol) {
        inputs.add(inputSymbol);
    }

    @Override
    public void removeInput(GateOrIO input) {
        inputs.remove(input);
    }

    @Override
    public LogicElement findLogic() {
        LogicElement result = getCorrespondingLogicElement();
        for (GateOrIO input : inputs) {
            result.addInput(input.findLogic());
        }
        return result;
    }

    protected abstract LogicElement getCorrespondingLogicElement();

    @Override
    public void update(InputCircuit parent) {
        outputCircle.update(parent.getDrawing());
        this.parent = parent;
    }

    @Override
    public void resetOutputLines() {
        outputCircle.reset();
    }
}