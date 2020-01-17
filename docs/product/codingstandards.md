<center>
<h1>Coding standards</h1>
</center>


## Coding standards
Below are described the standards for writing code.

Those are based on the [java's coding conventions](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf).

### Braces
Opening braces are always on a new line, and at the same indentation level as the declaration.
```java
int example( int a )
{
    return a + 1;
}
```

### Indentations
Indentations are 4 spaces long.

Elements on the same scope are on the same indentation level, elements in sub-scopes are an indentation level higher.

When a method or a statement is too long, it returns with a single additional indentation level.
When an operator is in between a too long statement, it goes to its own line with an additional indentation.

```java
void longMethodDeclaration 
    ( 
        int parameter1, 
        double parameter2, 
        String anotherparameter
    )
    {
        anotherparameter = 
            "" + parameter1 * 256 
                +
            parameter2 * parameter1;
    }
```

### Comments
Summarys of what the code below have a reserved line
```java
// says hello
String str = "Hello";
System.out.println(str);
```

Instruction specification are on the same line
```java
if (a<b||foo>el.param) // explaination on what this does
    return;
```


### Naming
#### classes

- classes follow the Camel case notation.
- Interfaces always begin with a capital `I` and it's always the first letter in an interface name.
- PCP specic classes start with capital `PCP`

#### methods
methods follow camel case with lower first letter.



## Documentation standards

A document title is written in html at the beginning og the document.

The highest header in the document is **h2**, written as `## Title` in markdown.

Before every section an additional empty line must be inserted.

```markdown
<center>
<h1>Title here</h1>
</center>

## Section
section content

### Subsection
subsection content

```