#include <stdio.h>
int main()
{
    double a,b,c;
    while(scanf("%lf%lf", &a, &b) != EOF)
    {
    if (b!=0)
    {
        c=a/b;
        printf("%.2f\n", c);
    }
    else
        printf("error\n");
    }

    return 0;
}

