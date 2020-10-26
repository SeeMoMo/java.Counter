package MoneyCalc;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.math.*;
  public class Main {
        public static void main(String[] args) {
            new Calculator("计算器");
        }
    }

    class Calculator extends JFrame {
        private TextArea show;  //  显示

        public Calculator(String t){
            super(t);
            setLayout(null);
            setSize(430,488);
            setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);
            this.setBackground(new Color(237, 239, 238));

            setShow();
            addBut();
            setVisible(true);
        }

        private void setShow() {
            show = new TextArea("",8,52,1);
            show.setSize(415,102);
            show.setLocation(0,0);
            show.setFont(new Font("", Font.BOLD, 40));
            show.setBackground(Color.WHITE);
            show.setForeground(new Color(48, 48, 48));
            show.setEditable(false);
            show.setText(0 + "");
            this.add(show);
        }

        private double nub1 = 0;    //  数1
        private double nub2 = 0;    //  数2
        private String snub2 = "";  //  数2的字符串型
        private int f = '+';        //  运算符号
        private boolean isDoc = false;  //  是否有小数点
        private boolean isEqual = false;    //  是否点击等于
        private int nubLen = 0;

        private void setBut(String s, int i, int j, int x, int y, final int c) {
            Button b = new Button(s);
            b.setLocation(x, y);
            b.setSize(i, j);
            b.setFont(new Font("", Font.BOLD, 35));
            b.setBackground(new Color(254, 254, 254));
            b.setForeground(new Color(88, 92, 100));

            //  计算逻辑算法
            //  Ljm ljm@lpqo.cn
            //  2020.6.23 JXUT
            b.addActionListener(e -> {
                if (c < 10) {
                    //    数字0-9
                    if (isEqual) {
                        nub1 = 0;
                    }
                    if (nubLen > 20) {
                        return;
                    }
                    if (c == 0 && nub2 == 0 ) {
                        return;
                    }
                    snub2 += String.valueOf(c);
                    show.setText(snub2);
                    nubLen = show.getText().length();
                    nub2 = Double.parseDouble(snub2);

                } else if (c == '.') {
                    //    小数
                    if (nubLen > 20) {
                        return;
                    }
                    if (!isDoc) {
                        if (nub2 != 0) {
                            snub2 += ".";
                        } else {
                            snub2 = "0.";
                        }
                        nub2 = Double.parseDouble(snub2);
                        show.setText(snub2);
                        isDoc = true;
                    }

                } else if (c == 'S') {
                    // 结果相反数
                    if (Double.parseDouble(show.getText()) == nub1) {
                        nub1 = 0 - nub1;
                        showf(nub1 +"");
                    } else {
                        nub2 = 0 -nub2;
                        showf(nub2 +"");
                    }
                } else if (c == 'C') {
                    // 归零
                    nub1 = 0;
                    nub2 = 0;
                    snub2 = "";
                    f = '+';
                    isDoc = false;
                    isEqual = false;
                    nubLen = 0;
                    showf("0");
                } else {
                    isEqual = false;
                    nubLen = 0;

                    switch (f) {
                        case '/':
                            //    除法
                            nub1 = DoubleUtil.divide(nub1, nub2);
                            break;
                        case '*':
                            // 乘法
                            nub1 = DoubleUtil.mul(nub1, nub2);
                            break;
                        case '-':
                            // 减法
                            nub1 = DoubleUtil.sub(nub1, nub2);
                            break;
                        case '+':
                            //    加法
                            nub1 = DoubleUtil.add(nub1, nub2);
                            break;
                    }

                    showf(nub1 + "");
                    if (c != '=') {
                        f = c;
                    } else {
                        // 等于
                        f = '+';
                        isEqual = true;
                    }

                    nub2 = 0;
                    snub2 = "";
                    isDoc = false;
                }
            });
            this.add(b);
        }


        //    去0小数
        private void showf(String s ){
            double td = Double.parseDouble(s);
            int ti = (int)td;

            if (td == ti) {
                show.setText(ti +"");
            } else {
                show.setText(td + "");
            }
        }

        private void addBut() {
            setBut("AC", 215, 70, 0, 100, 'C');
            setBut("±", 107, 70, 214, 100, 'S');
            setBut("÷", 108, 70, 312, 100, '/');

            setBut("7", 107, 70, 0, 170, 7);
            setBut("8", 108, 70, 107, 170, 8);
            setBut("9", 107, 70, 214, 170, 9);
            setBut("×", 108, 70, 312, 170, '*');

            setBut("4", 107, 70, 0, 240, 4);
            setBut("5", 108, 70, 107, 240, 5);
            setBut("6", 107, 70, 214, 240, 6);
            setBut("-", 108, 70, 312, 240, '-');

            setBut("1", 107, 70, 0, 310, 1);
            setBut("2", 108, 70, 107, 310, 2);
            setBut("3", 107, 70, 214, 310, 3);
            setBut("+", 108, 70, 312, 310, '+');

            setBut("0", 107, 70, 0, 380, 0);
            setBut(".", 108, 70, 107, 380, '.');
            setBut("=", 215, 70, 214, 380, '=');
        }

    }

    //  BigDecimal的Double精确计算
    class DoubleUtil implements Serializable {
        private static final long serialVersionUID = -3345205828566485102L;
        private static final Integer DEF_DIV_SCALE = 40;    //  除法精度

        //  精确加
        public static Double add(Double value1, Double value2) {
            BigDecimal b1 = new BigDecimal(Double.toString(value1));
            BigDecimal b2 = new BigDecimal(Double.toString(value2));
            return b1.add(b2).doubleValue();
        }

        //  精确减
        public static double sub(Double value1, Double value2) {
            BigDecimal b1 = new BigDecimal(Double.toString(value1));
            BigDecimal b2 = new BigDecimal(Double.toString(value2));
            return b1.subtract(b2).doubleValue();
        }

        //  精确乘
        public static Double mul(Double value1, Double value2) {
            BigDecimal b1 = new BigDecimal(Double.toString(value1));
            BigDecimal b2 = new BigDecimal(Double.toString(value2));
            return b1.multiply(b2).doubleValue();
        }

        //  精确除 精度为 DEF_DIV_SCALE
        public static Double divide(Double dividend, Double divisor) {
            return divide(dividend, divisor, DEF_DIV_SCALE);
        }

        //  精确除
        public static double divide(Double dividend, Double divisor, Integer scale) {
            if (scale < 0) {
                return -1;
            }
            BigDecimal b1 = new BigDecimal(Double.toString(dividend));
            BigDecimal b2 = new BigDecimal(Double.toString(divisor));
            return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
        }

    }

