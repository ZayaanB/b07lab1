import java.lang.Math;

public class Polynomial
{
    double[] coefficients;

    public Polynomial() 
    {
        coefficients = new double[]{0};
    }

    public Polynomial(double[] coefficients)
    {
        this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial other_polynomial)
    {
        int length = Math.max(this.coefficients.length, other_polynomial.coefficients.length);
        double[] result = new double[length];

        for (int i = 0; i < this.coefficients.length; i++) {
            result[i] = this.coefficients[i];
        }

        for (int i = 0; i < other_polynomial.coefficients.length; i++) {
            result[i] += other_polynomial.coefficients[i];
        }

        return new Polynomial(result);
    }

    public double evaluate(double x)
    {
        double total = 0;
        int length = this.coefficients.length;

        for(int i = 0; i < length; i++)
        {
            total += this.coefficients[i]*Math.pow(x, i);
        }

        return total;
    }

    public boolean hasRoot(double solution)
    {
        return this.evaluate(solution) == 0;
    }
}