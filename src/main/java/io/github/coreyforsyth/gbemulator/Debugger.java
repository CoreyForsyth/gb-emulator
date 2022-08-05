package io.github.coreyforsyth.gbemulator;

import io.github.coreyforsyth.gbemulator.instruction.Instructions;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Debugger extends JFrame
{
    private CPU cpu;

    public Debugger(CPU initialCPU) {
        super("Debugger");
        cpu = initialCPU;

        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir") + "/src/main/resources");

//        jPanel.setPreferredSize(new Dimension(600, 400));
        RegisterPanel registerPanel = new RegisterPanel(cpu);

        JButton romSelect = new JButton("Select GB");
        romSelect.addActionListener(a -> {
            int i = jFileChooser.showOpenDialog(jPanel);
            if (i == JFileChooser.APPROVE_OPTION) {
                cpu = RomLoader.initCpu(jFileChooser.getSelectedFile());
                registerPanel.setCpu(cpu);
                registerPanel.updateValues();
            }
        });
        jPanel.add(romSelect, constraints);
        JButton nextInstruction = new JButton("Next");
        nextInstruction.addActionListener(a -> {
            executeInstructions(cpu, 1);
            registerPanel.updateValues();
        });
        constraints.gridx = 1;
        jPanel.add(nextInstruction, constraints);


        JTextField instructionCount = new JTextField("1");
        JButton skipInstruction = new JButton("Next n");
        skipInstruction.addActionListener(a -> {
            int count = Integer.parseInt(instructionCount.getText());
            executeInstructions(cpu, count);
            registerPanel.updateValues();
        });
        constraints.gridy = 1;
        constraints.gridx = 0;
        jPanel.add(instructionCount, constraints);
        constraints.gridx = 1;
        jPanel.add(skipInstruction, constraints);

        JTextField nextOpcode = new JTextField("00");
        JButton executeUntilButton = new JButton("Until");
        executeUntilButton.addActionListener(a -> {
            byte opcode = (byte) Integer.parseInt(nextOpcode.getText(), 16);
            executeUntilNextOccurence(cpu, opcode);
            registerPanel.updateValues();
        });
        constraints.gridy = 2;
        constraints.gridx = 0;
        jPanel.add(nextOpcode, constraints);
        constraints.gridx = 1;
        jPanel.add(executeUntilButton, constraints);


        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        jPanel.add(registerPanel, constraints);

        this.setContentPane(jPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        registerPanel.updateValues();
        this.setVisible(true);
    }

    public void executeInstructions(CPU cpu, int count) {
//        log.info("Executing {} instructions", count);
        for (int i = 0; i < count; i++)
        {
            Instructions.next(cpu);
        }
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
