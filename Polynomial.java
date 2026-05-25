import java.lang.Math;
import java.util.Scanner;
import java.io.*;

public class Polynomial
{
    double[] coefficients;
    int[] exponents;

    public Polynomial() 
    {
        coefficients = new double[]{0};
        exponents = new int[]{0};
    }

    public Polynomial(double[] coefficients)
    {
        int length = coefficients.length;
        int j = 0;

        for (int i = 0; i < length; i++)
        {
            if(coefficients[i] != 0)
            {
                j++;
            }
        }
        this.coefficients = new double[j];
        exponents = new int[j];


        j = 0;
        for(int i = 0; i < length; i++)
        {
            if(coefficients[i] != 0)
            {
                this.coefficients[j] = coefficients[i];
                exponents[j] = i;
                j++;
            }
        }
    }

    public Polynomial add(Polynomial other_polynomial)
    {
        int maxExp = 0;
        
        if(this.exponents.length == 0)
        {
            maxExp = other_polynomial.exponents[other_polynomial.exponents.length - 1];
        }
        else if(other_polynomial.exponents.length == 0)
        {
            maxExp = this.exponents[this.exponents.length - 1];
        }
        else
        {
            maxExp = Math.max(this.exponents[this.exponents.length - 1], other_polynomial.exponents[other_polynomial.exponents.length - 1]);
        }
        double[] sum = new double[maxExp + 1];

        for (int i = 0; i < this.coefficients.length; i++) {
            sum[this.exponents[i]] += this.coefficients[i];
        }

        for (int i = 0; i < other_polynomial.coefficients.length; i++) {
            sum[other_polynomial.exponents[i]] += other_polynomial.coefficients[i];
        }

        return new Polynomial(sum);
    }

    public Polynomial(File file)
    {
        // NOTE: I will assume 1s and specifically denoted for simplicity sake (ie. x = 1x1)
        try(Scanner fileReader = new Scanner(file);)
        {
            // handle empty file
            if(!fileReader.hasNextLine())
            {
                fileReader.close();
                this.coefficients = new double[0];
                this.exponents = new int[0];
            }

            String polynomial = fileReader.nextLine();
            fileReader.close();

            // consider - has +-# 
            String s = polynomial.replace("-", "+-");
            if(s.startsWith("+-"))
            {
                s = s.substring(1);
            }

            // split terms
            String[] terms = s.split("\\+");
            
            int numTerms = 0;
            for (String term : terms) {
                if (!term.isEmpty()) {
                    numTerms++;
                }
            }

            this.coefficients = new double[numTerms];
            this.exponents = new int[numTerms];

            int length = terms.length;
            double coeff;
            int exp;
            int idx = 0;

            for (int i = 0; i < length; i++) {
                String termStr = terms[i];
                if (termStr.isEmpty()) {
                    continue;
                }

                if (termStr.contains("x")) 
                {
                    String[] parts = termStr.split("x");
                    if (parts.length == 0 || parts[0].isEmpty())
                    {
                        coeff = 1.0;
                    }
                    else if (parts[0].equals("-"))
                    {
                        coeff = -1.0;
                    }
                    else 
                    {
                        coeff = Double.parseDouble(parts[0]);
                    }

                    if (parts.length > 1 && !parts[1].isEmpty()) 
                    {
                        exp = Integer.parseInt(parts[1]);
                    }
                    else 
                    {
                        exp = 1;
                    }
                } 
                else 
                {
                    coeff = Double.parseDouble(termStr);
                    exp = 0;
                }
                
                this.coefficients[idx] = coeff;
                this.exponents[idx] = exp;
                idx++;
            }

            // selection sort
            int min_idx;
            int tempExp;
            double tempCoeff;
            for (int i = 0; i < numTerms - 1; i++) {
                min_idx = i;
                for (int j = i + 1; j < numTerms; j++) {
                    if (this.exponents[j] < this.exponents[min_idx]) {
                        min_idx = j;
                    }
                }

                tempExp = this.exponents[min_idx];
                this.exponents[min_idx] = this.exponents[i];
                this.exponents[i] = tempExp;

                tempCoeff = this.coefficients[min_idx];
                this.coefficients[min_idx] = this.coefficients[i];
                this.coefficients[i] = tempCoeff;
            }
            
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Error finding or reading the file.");
        }
    }

    public double evaluate(double x)
    {
        double total = 0;
        int length = this.coefficients.length;

        for(int i = 0; i < length; i++)
        {
            total += this.coefficients[i]*Math.pow(x, exponents[i]);
        }

        return total;
    }

    public boolean hasRoot(double solution)
    {
        return this.evaluate(solution) == 0;
    }

    public Polynomial multiply(Polynomial other_polynomial)
    {
        int length1 = this.coefficients.length;
        int length2 = other_polynomial.coefficients.length;
        int sumExp;
        double prodCoeffs;

        if (length1 == 0 || length2 == 0) {
            return new Polynomial(new double[]{0});
        }

        int maxExp1 = this.exponents[this.exponents.length - 1];
        int maxExp2 = other_polynomial.exponents[other_polynomial.exponents.length - 1];
        int maxExp = maxExp1 + maxExp2;

        double[] result = new double[maxExp + 1];

        for(int i = 0; i < length1; i++)
        {
            for(int j = 0; j < length2; j++)
            {
                sumExp = this.exponents[i] + other_polynomial.exponents[j];
                prodCoeffs = this.coefficients[i] * other_polynomial.coefficients[j];
                result[sumExp] += prodCoeffs;
            }
        }

        return new Polynomial(result);
    }

    public void saveToFile(File file)
    {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));)
        {
            String s = "";
            int length = this.coefficients.length;
            double coeff;
            int exp;

            for(int i = 0; i < length; i++)
            {
                // speed change
                coeff = this.coefficients[i];
                exp = this.exponents[i];

                // handling adding
                if(i != 0 && coeff > 0)
                {
                    s += "+";
                }

                // handling exponent cases
                if(exp == 0)
                {
                    s += coeff;
                }
                else if(exp == 1)
                {
                    // handling +/- 1 with proper notation
                    if(coeff == 1)
                    {
                        s += "x";
                    }
                    else if(coeff == -1)
                    {
                        s += "-x";
                    }
                    else
                    {
                        s += coeff + "x";   
                    }
                }
                else 
                {
                    // handling +/- 1 with proper notation
                    if(coeff == 1)
                    {
                        s += "x" + exp;
                    }
                    else if(coeff == -1)
                    {
                        s += "-x" + exp;
                    }
                    else
                    {
                        s += coeff + "x" + exp;
                    }
                }
            }

            fileWriter.write(s);
            fileWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("Error, file not found");
        }
    }

    // testing function
    @Override
    public String toString()
    {
        String s = "";
        int length = this.coefficients.length;
        double coeff;
        int exp;

        for(int i = 0; i < length; i++)
        {
            // speed change
            coeff = this.coefficients[i];
            exp = this.exponents[i];

            // handling adding
            if(i != 0 && coeff > 0)
            {
                s += "+";
            }

            // handling exponent cases
            if(exp == 0)
            {
                s += coeff;
            }
            else if(exp == 1)
            {
                // handling +/- 1 with proper notation
                if(coeff == 1)
                {
                    s += "x";
                }
                else if(coeff == -1)
                {
                    s += "-x";
                }
                else
                {
                    s += coeff + "x";   
                }
            }
            else 
            {
                // handling +/- 1 with proper notation
                if(coeff == 1)
                {
                    s += "x" + exp;
                }
                else if(coeff == -1)
                {
                    s += "-x" + exp;
                }
                else
                {
                    s += coeff + "x" + exp;
                }
            }
        }
        return s;
    }
}