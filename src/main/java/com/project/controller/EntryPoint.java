package com.project.controller;

import javax.swing.*;

public class EntryPoint {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Application()::start);
    }
}
