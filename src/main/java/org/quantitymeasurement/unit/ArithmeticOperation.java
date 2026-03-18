package org.quantitymeasurement.unit;

public enum ArithmeticOperation {
    ADD("+") {
        @Override
        public double compute(double a, double b) {
            return a + b;
        }
    },
    SUBTRACT("-") {
        @Override
        public double compute(double a, double b) {
            return a - b;
        }
    },
    DIVIDE("/") {
        @Override
        public double compute(double a, double b) {
            if (b == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        }
    },
    MULTIPLY("*") {
        @Override
        public double compute(double a, double b) {
            return a * b;
        }
    },
    NONE("") {
        @Override
        public double compute(double a, double b) {
            throw new UnsupportedOperationException("NONE operation does not support computation");
        }
    };

    private final String symbol;

    ArithmeticOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static ArithmeticOperation fromSymbol(String symbol) {
        for (ArithmeticOperation op : ArithmeticOperation.values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        return NONE;
    }

    public abstract double compute(double a, double b);
}
