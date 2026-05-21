public class Polynomial
{
    double[] coefficients;

    public Polynomial() 
    {
        coefficients = new double[]{0};
    }

    public Polynomial(double coefficients)
    {
        this.coefficients = coefficients;
    }

    public void add(Polynomial other_Polynomial)
    {
        length = min(len(other_polynomial.coefficients), len(this.coefficients));

        for(int i = 0; i < length; i++)
        {
            this.coefficients[i] += other_polynomial.coefficients;
        }
    }

    public double evaluate(double x)
    {
        double total = 0;
        length = len(this.coefficients);

        for(int i = 0; i < length; i++)
        {
            total += this.coefficients[i]*x;
        }

        return total;
    }

    public bool hasRoot(double solution)
    {
        int length = len(this.coefficients);
        double total = 0;

        for(int i = 0; i < length; i++)
        {
            total += this.coefficients[i]*Math.pow(solution, i);
        }

        return total == 0;
    }
}