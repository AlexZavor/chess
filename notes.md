# Java Fundamentals

### Javadoc
auto documentation
Takes comments from code to make webpage

/**
    javadoc description for class.
    @return return
*/
public class MyClass{

    /**
        javadoc description for method
        up to first '.' is summary
    */
    public void myMethod(){

    }
}

### Primitives
byte        - 8
short       - 16
int         - 32
long        - 64
float       
double      
char        - 'a' 16 bit \u for unicode identitys
boollean    - not 1/0. different datatype

### Output
System.out.println(int + test + ", " + long1);
System.out.printf("%d, %d, %d, %d \n", int, test, long1);

### Conversion
int Integer.parseInt(String value)
returns exception for incorrectly formated thing
Wrapper class for all of the primitives. with parse methods

### Strings
Technically not primitives
Immutable
Concatinations always make a new string.
String.format()                     // like printf() function for formating a string.
String s = "Hello"                  // pointing to the same "hello" for the same string.
String s = new String("Hello");     // Same literal. makes a copy. not really helpful
String concatenation is slow
    so use a StringBuilder instead
    builder = new StringBuilder();
    builder.append
    String str = builder.toString();

### Arrays
int [] intArray;            // is an object. not primitive, reference to an array
intArray = new int[10];     // actually makes the array. points the reference to it
intArray[0] = 500;          // initialize

int [] intArray2 = {1, 2, 3, 4}; // All at once
They actually know how big they are
intArray.length

for (int value : intArray2){
    System.out.print(value);
}

multidementional arrays
the arrays inside can be different sizes.