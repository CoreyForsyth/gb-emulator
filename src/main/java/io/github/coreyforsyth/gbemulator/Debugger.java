package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.graphics.ScreenPanel;
import io.github.coreyforsyth.gbemulator.instruction.Instruction;
import io.github.coreyforsyth.gbemulator.instruction.Instructions;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class Debugger extends JFrame
{
    private CPU cpu;
    final ScreenPanel screenPanel;
    private volatile boolean run = false;
    private volatile long lastCycleTime;
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
	private final JTextField charAddress;
	private final JTextField byteValue;
    private final JTable table;




    public Debugger(CPU initialCPU) {
        super("Debugger");

        cpu = initialCPU;
        screenPanel = new ScreenPanel(cpu);
        cpu.logState();
        MigLayout migLayout = new MigLayout("wrap 5");
        JPanel jPanel = new JPanel(migLayout);
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));


		JButton update = new JButton("Update");
		update.addActionListener(l -> updateRegisterValues());
        JButton romSelect = new JButton("Select GB");
        romSelect.addActionListener(a -> {
            int i = jFileChooser.showOpenDialog(jPanel);
            if (i == JFileChooser.APPROVE_OPTION) {
                cpu = RomLoader.initCpu(jFileChooser.getSelectedFile());
                screenPanel.setCpu(cpu);
                cpu.logState();
                updateRegisterValues();
            }
        });
        JButton nextInstruction = new JButton("Next");
        nextInstruction.addActionListener(a -> {
            executeInstructions(cpu, 1);
            updateRegisterValues();
        });


		SpinnerNumberModel model = new SpinnerNumberModel(100, 1, 2000000, 1);
		JSpinner instructionCount = new JSpinner(model);
        JButton skipInstruction = new JButton("Next n");
        skipInstruction.addActionListener(a -> {
            executeInstructions(cpu, model.getNumber().intValue());
            updateRegisterValues();
        });

        JTextField nextOpcode = new JTextField("00");
        JButton executeUntilButton = new JButton("Until");
        executeUntilButton.addActionListener(a -> {
			char opcode = (char) Integer.parseInt(nextOpcode.getText(), 16);
            executeUntilNextOccurence(cpu, opcode);
            updateRegisterValues();
        });


		JTextField nextLy = new JTextField("91");
		JButton nextFrame = new JButton("Next Frame");
		nextFrame.addActionListener(a -> {
			executeUntilNextFrame(cpu, Integer.parseInt(nextLy.getText(), 16));
			updateRegisterValues();
		});
        JButton next10Frames = new JButton("Next Frames");
        next10Frames.addActionListener(a -> {
            for (int i = 0; i < 10; i++)
            {
                executeUntilNextFrame(cpu, 0x91);
            }
            updateRegisterValues();
        });


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
		charAddress = new JTextField("00");
		byteValue = new JTextField();

		JButton updateScreen = new JButton("Update Screen");
		updateScreen.addActionListener(l -> {
			screenPanel.update();
		});
		JCheckBox debug = new JCheckBox("Debug");
		debug.addChangeListener(l -> {
			boolean selected = debug.isSelected();
			cpu.setDebug(selected);
		});
        JCheckBox runCheckBox = new JCheckBox("Run");
        runCheckBox.addChangeListener(l -> {
            run = runCheckBox.isSelected();
        });

        jPanel.add(romSelect, "skip");
        jPanel.add(nextInstruction);
        jPanel.add(screenPanel, "skip, span 1 9");
        jPanel.add(instructionCount, "skip");
        jPanel.add(skipInstruction, "wrap");
        jPanel.add(nextOpcode, "skip");
        jPanel.add(executeUntilButton, "wrap");

		jPanel.add(nextLy, "skip");
		jPanel.add(nextFrame);
		jPanel.add(next10Frames, "wrap");

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
        table = new JTable(new Object[16][4], new String[]{"Addr", "Data", "Inst", "Params"});
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        table.setRowHeight(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(0);
        table.getColumnModel().getColumn(2).setPreferredWidth(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setPreferredSize(new Dimension(256, 256));
        setupTable(cpu.getPC());
        jPanel.add(table, "span 1 9");

		jPanel.add(update);
		jPanel.add(charAddress);
		jPanel.add(byteValue);


		jPanel.add(updateScreen);
		jPanel.add(debug);
        JButton nextInstructions = new JButton("Next instructions");
        nextInstructions.addActionListener(l -> {
            char instructionAddress = cpu.getPC();
            for (int i = 0; i < 10; i++)
            {
                byte opCode = cpu.readByte(instructionAddress);
                Instruction<?, ?> instruction = Instructions.getInstruction(opCode);
                instructionAddress += instruction.size();
            }
        });
        jPanel.add(nextInstructions);
        jPanel.add(runCheckBox);




        this.setContentPane(jPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        updateRegisterValues();
        this.setVisible(true);

        lastCycleTime = System.nanoTime();

        new Thread(() -> {
            while (true)
            {
                Thread.onSpinWait();
                if (run) {
                    long currentTime = System.nanoTime();
                    if (currentTime - lastCycleTime > 2000) {
                        nextInstruction(cpu);
                        lastCycleTime = currentTime;
                    }
                }

            }
        }).start();
    }

	public void executeUntilNextFrame(CPU cpu, int lyc) {
        byte ly;
        for (int i = 0; i < 65000; i++)
		{
            ly = cpu.getDisplay().getLy();
			if ((ly & 0xFF) != lyc) {
				break;
			}
			nextInstruction(cpu);
		}
		for (int i = 0; i < 65000; i++)
		{
            ly = cpu.getDisplay().getLy();
			if ((ly & 0xFF) == lyc) {
				break;
			}
			nextInstruction(cpu);
		}
	}

    public void executeInstructions(CPU cpu, int count) {
        for (int i = 0; i < count; i++)
        {
			nextInstruction(cpu);
        }
    }

	private void nextInstruction(CPU cpu) {
		Instructions.next(cpu);
        if (cpu.getDisplay().isImageReady()) {
            cpu.getDisplay().setImageReady(false);
            screenPanel.update();
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
			String text = charAddress.getText();
			int i = Integer.parseInt(text, 16);
			byteValue.setText(Integer.toHexString(cpu.readByte((char) i) & 0xFF).toUpperCase());
            resetTable(cpu.getPC());
		});
    }

    public void resetTable(char instructionAddress) {
        int startAddress = Integer.parseInt((String) table.getValueAt(0, 0), 16);
        int endAddress = Integer.parseInt((String) table.getValueAt(12, 0), 16);
        if (instructionAddress >= startAddress && instructionAddress <= endAddress) {
            for (int i = 0; i <= 12; i++)
            {
                int parsedInstructionAddress = Integer.parseInt((String) table.getValueAt(i, 0), 16);
                if (parsedInstructionAddress >= instructionAddress) {
                    table.setRowSelectionInterval(i, i);
                    break;
                }
            }
            return;
        }

        setupTable(instructionAddress);
    }

    private void setupTable(char instructionAddress)
    {
        for (int i = 0; i < 16; i++)
        {
            table.setValueAt(String.format("%04X", instructionAddress & 0xFFFF), i, 0);

            byte opCode = cpu.readByte(instructionAddress);
            Instruction<?, ?> instruction = Instructions.getInstruction(opCode);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%02X", opCode));
            if (instruction.size() >= 2) {
                sb.append(String.format("%02X", cpu.readByte((char) (instructionAddress + 1))));
            }
            if (instruction.size() == 3) {
                sb.append(String.format("%02X", cpu.readByte((char) (instructionAddress + 2))));
            }
            table.setValueAt(sb.toString(), i, 1);

            table.setValueAt(instruction.getClass().getSimpleName(), i, 2);

            sb = new StringBuilder();
            if (instruction.getPrimary() != null) {
                sb.append(instruction.getPrimary().friendlyName(cpu, instructionAddress));
            }
            if (instruction.getSecondary() != null) {
                sb.append(", ").append(instruction.getSecondary().friendlyName(cpu, instructionAddress));
            }
            table.setValueAt(sb.toString(), i, 3);

            instructionAddress += instruction.size();
        }
        table.setRowSelectionInterval(0, 0);
    }

    public void executeUntilNextOccurence(CPU cpu, char address) {
        int count = 0;
        while (cpu.getPC() != address) {
            count++;
			nextInstruction(cpu);
        }
        if (count >= 100000) {
            System.out.printf("executed 100 times without finding opcode %04X\n", (int) address & 0xFFFF);
        }
    }


}
