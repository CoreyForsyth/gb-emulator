package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.graphics.ScreenPanel;
import io.github.coreyforsyth.gbemulator.instruction.Instructions;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class Debugger extends JFrame
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

    public Debugger(CPU initialCPU) {
        super("Debugger");

        cpu = initialCPU;
        MigLayout migLayout = new MigLayout("wrap 5");
        JPanel jPanel = new JPanel(migLayout);
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir") + "/src/main/resources");

//        jPanel.setPreferredSize(new Dimension(600, 400));

        JButton romSelect = new JButton("Select GB");
        romSelect.addActionListener(a -> {
            int i = jFileChooser.showOpenDialog(jPanel);
            if (i == JFileChooser.APPROVE_OPTION) {
                cpu = RomLoader.initCpu(jFileChooser.getSelectedFile());
                updateRegisterValues();
            }
        });
        JButton nextInstruction = new JButton("Next");
        nextInstruction.addActionListener(a -> {
            executeInstructions(cpu, 1);
            updateRegisterValues();
        });


		SpinnerNumberModel model = new SpinnerNumberModel(100, 1, 10000, 1);
		JSpinner instructionCount = new JSpinner(model);
        JButton skipInstruction = new JButton("Next n");
        skipInstruction.addActionListener(a -> {
            executeInstructions(cpu, model.getNumber().intValue());
            updateRegisterValues();
        });

        JTextField nextOpcode = new JTextField("00");
        JButton executeUntilButton = new JButton("Until");
        executeUntilButton.addActionListener(a -> {
            byte opcode = (byte) Integer.parseInt(nextOpcode.getText(), 16);
            executeUntilNextOccurence(cpu, opcode);
            updateRegisterValues();
        });
        ScreenPanel screenPanel = new ScreenPanel(cpu);

        JButton aButton = new JButton(" A");
        a = new JTextField();
        f = new JTextField();
        JButton fButton = new JButton("znhc");
        JButton bButton = new JButton(" B");
        b = new JTextField();
        c = new JTextField();
        JButton cButton = new JButton("C ");
        JButton dButton = new JButton(" D");
        d = new JTextField();
        e = new JTextField();
        JButton eButton = new JButton("E ");
        JButton hButton = new JButton(" H");
        h = new JTextField();
        l = new JTextField();
        JButton lButton = new JButton("L ");
        JButton spButton = new JButton("SP");
        sp = new JTextField();
        JButton pcButton = new JButton("PC");
        pc = new JTextField();
        nextInst = new JTextField();
		JButton updateScreen = new JButton("Update Screen");
		updateScreen.addActionListener(l -> {
			screenPanel.update();
		});
		JCheckBox debug = new JCheckBox("Debug");
		debug.addChangeListener(l -> {
			boolean selected = debug.isSelected();
			cpu.setDebug(selected);
		});

        jPanel.add(romSelect, "skip");
        jPanel.add(nextInstruction);
        jPanel.add(screenPanel, "skip, span 1 10");
        jPanel.add(instructionCount, "skip");
        jPanel.add(skipInstruction, "wrap");
        jPanel.add(nextOpcode, "skip");
        jPanel.add(executeUntilButton, "wrap");

        jPanel.add(aButton);
        jPanel.add(a);
        jPanel.add(f);
        jPanel.add(fButton);
        jPanel.add(bButton);
        jPanel.add(b);
        jPanel.add(c);
        jPanel.add(cButton);
        jPanel.add(dButton);
        jPanel.add(d);
        jPanel.add(e);
        jPanel.add(eButton);
        jPanel.add(hButton);
        jPanel.add(h);
        jPanel.add(l);
        jPanel.add(lButton);
        jPanel.add(spButton);
        jPanel.add(sp, "span 2, growx");
        jPanel.add(new JLabel("nothing"));
        jPanel.add(pcButton);
        jPanel.add(pc, "span 2, growx");
        jPanel.add(nextInst);
		jPanel.add(updateScreen);
		jPanel.add(debug);


        this.setContentPane(jPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        updateRegisterValues();
        this.setVisible(true);
    }

    public void executeInstructions(CPU cpu, int count) {
        for (int i = 0; i < count; i++)
        {
            Instructions.next(cpu);
        }
    }

    public void updateRegisterValues() {
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

    public void executeUntilNextOccurence(CPU cpu, byte opcode) {
//        log.info("Executing until opcode: {}", String.format("%02X", opcode));
        int count = 0;
        while (cpu.peakNextInstruction() != opcode && count < 100) {
            count++;
            Instructions.next(cpu);
        }
        if (count >= 100) {
            log.info("executed 100 times without finding opcode {}", String.format("%02X", opcode));
        }
    }


}
