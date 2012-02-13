package brainfuckcompiler.compiler.program.statements;

import brainfuckcompiler.compiler.expressions.ExpressionGenerator;
import brainfuckcompiler.compiler.expressions.Node;
import brainfuckcompiler.compiler.expressions.nodes.AssignmentOperator;
import brainfuckcompiler.compiler.expressions.nodetypes.SubNode;
import brainfuckcompiler.compiler.program.structure.Block;
import brainfuckcompiler.compiler.program.structure.Item;
import brainfuckcompiler.compiler.program.structure.Line;
import brainfuckcompiler.statics;
import java.util.ArrayList;

public class ExpressionStatement extends Statement
{

    Node expression;

    public ExpressionStatement(Block parentBlock, int LineNumber)
    {
        super(parentBlock, LineNumber);
    }

    @Override
    public int parseStatement(ArrayList<Item> items, int currentPosition)
    {
        expression = ExpressionGenerator.generateExpression(((Line) items.get(currentPosition)).getLine(), lineNumber, parentBlock);
        if (!((expression instanceof AssignmentOperator) || (expression instanceof SubNode)))
        {
            System.out.println("Expression on line " + lineNumber + " must be an assignment");
            System.exit(0);
        }
        currentPosition++;
        return currentPosition;
    }

    @Override
    public void generate()
    {
        if (expression instanceof SubNode)
        {
            SubNode s = (SubNode) expression;
            if (s.getType() == SubNode.SUB)
            {
                s.generateBF();
                return;
            } else if (s.getType() == SubNode.FUNC)
            {
                int ret = s.generateBF();
                statics.t.clear(ret);
                statics.t.free(ret);
                return;
            } else if (s.getType() == SubNode.NOTFOUND)
            {
                System.out.println("\"" + s.getSubName() + "\" not found on line " + lineNumber);
            }
        }
        expression.generateBF();
    }
}