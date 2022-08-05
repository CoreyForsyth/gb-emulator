package io.github.coreyforsyth.gbemulator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterPanel extends JPanel
{
    private CPU cpu;

    private final JTextField a;
    private final JTextField f;
    private final JTextField b;
    private final JTextField c;
    private final JTextField d;
    private final JTextField e;
    private final JTextField h;
    private final JTextField l;
    private final JTextField sp;
    private final JTextField pc;
    private final JTextField nextInst;

    public RegisterPanel(CPU cpu) {
        super(new GridBagLayout());
        this.cpu = cpu;
//        this.setPreferredSize(new Dimension(250, 250));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JButton aButton = new JButton(" A");
        this.add(aButton, constraints);

        a = new JTextField();
        constraints.gridx = 1;
        this.add(a, constraints);

        f = new JTextField();
        constraints.gridx = 2;
        this.add(f, constraints);

        JButton fButton = new JButton("znhc");
        constraints.gridx = 3;
        this.add(fButton, constraints);

        constraints.gridy = 1;

        JButton bButton = new JButton(" B");
        constraints.gridx = 0;
        this.add(bButton, constraints);

        b = new JTextField();
        constraints.gridx = 1;
        this.add(b, constraints);

        c = new JTextField();
        constraints.gridx = 2;
        this.add(c, constraints);

        JButton cButton = new JButton("C ");
        constraints.gridx = 3;
        this.add(cButton, constraints);

        constraints.gridy = 2;

        JButton dButton = new JButton(" D");
        constraints.gridx = 0;
        this.add(dButton, constraints);

        d = new JTextField();
        constraints.gridx = 1;
        this.add(d, constraints);

        e = new JTextField();
        constraints.gridx = 2;
        this.add(e, constraints);

        JButton eButton = new JButton("E ");
        constraints.gridx = 3;
        this.add(eButton, constraints);

        constraints.gridy = 3;

        JButton hButton = new JButton(" H");
        constraints.gridx = 0;
        this.add(hButton, constraints);

        h = new JTextField();
        constraints.gridx = 1;
        this.add(h, constraints);

        l = new JTextField();
        constraints.gridx = 2;
        this.add(l, constraints);

        JButton lButton = new JButton("L ");
        constraints.gridx = 3;
        this.add(lButton, constraints);

        constraints.gridy = 4;

        JButton spButton = new JButton("SP");
        constraints.gridx = 0;
        this.add(spButton, constraints);

        sp = new JTextField();
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        this.add(sp, constraints);

        constraints.gridy = 5;

        JButton pcButton = new JButton("PC");
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        this.add(pcButton, constraints);

        pc = new JTextField();
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        this.add(pc, constraints);

        nextInst = new JTextField();
        constraints.gridx = 3;
        constraints.gridwidth = 1;
        this.add(nextInst, constraints);


    }

    public void updateValues() {
        SwingUtilities.invokeLater(() -> {
            a.setText(String.format("%02X", cpu.getA()));
            String fText = cpu.isZero() ? "1 " : "0 ";
            fText += cpu.isSubtraction() ? "1 " : "0 ";
            fText += cpu.isHalfCarry() ? "1 " : "0 ";
            fText += cpu.isCarry() ? "1 " : "0 ";
            f.setText(fText);
            b.setText(String.format("%02X", cpu.getB()));
            c.setText(String.format("%02X", cpu.getC()));
            d.setText(String.format("%02X", cpu.getD()));
            e.setText(String.format("%02X", cpu.getE()));
            h.setText(String.format("%02X", cpu.getH()));
            l.setText(String.format("%02X", cpu.getL()));
            sp.setText(String.format("%04X", (int) cpu.getSP()));
            pc.setText(String.format("%04X", (int) cpu.getPC()));
            nextInst.setText(String.format("%02X", cpu.peakNextInstruction()));
        });
    }

    public CPU getCpu()
    {
        return cpu;
    }

    public void setCpu(CPU cpu)
    {
        this.cpu = cpu;
    }
}
