package kmeans;

public class Record {
    int type;
    double ri; // refractive index
    double na; // sodium
    double mg; // magnesium
    double al; // aluminium
    double si; // silicon
    double k; // potassium
    double ca; // calcium
    double ba; // barium
    double fe; // iron

    public Record(int type, double ri, double na, double mg, double al, double si, double k, double ca, double ba, double fe) {
        this.type = type;
        this.ri = ri;
        this.na = na;
        this.mg = mg;
        this.al = al;
        this.si = si;
        this.k = k;
        this.ca = ca;
        this.ba = ba;
        this.fe = fe;
    }
}
