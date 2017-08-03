# Whiteboard

This is where I come to write ideas.

## Function Inheritance

Because it's flat leveled, things can be cached just fine.
Everything inherits from 'default'.  This is important because we can use it to establish language defaulting.
`/kotlin-expression` -> `/kotlin-statement` -> `/default`
This can also be used to throw errors for certain languages, stating it's not supported, or replacing it with some filler text for human-read translations.




## Functions

### Declaration and usage are the same

Call extends the declaration, and a call does the declaring if it hasn't been used.

### Declaration and usage are separate

The declaration must be in a package construct, and you must use a call construct to call the function.

## Validation

Do references fix the validation problems?

You only would validate proper definitions - inherited copies would not be checked, nor would anything of the like.
Could you accidentally slip into a recursive check?

The problem now is dealing with the problem of arguments in execution.

```yaml
breaker: 
  /killer-language:
    =: =breaker
    /: /interpret
    argument: 0
```


## Methods for Validation

### Options
- Create virtual dummy based of requirements
	- Requires requirements organized by argument
- Use dummy as requirements
	- Requirements can only really include preset things
		- Assert expression extends X
		- Assert expression has a certain property? I'm not sure how equivalence would be checked
			
### Requirements
- Compile Time
	- Must extend construct
	- Must have an element of a certain key where <X>
	- Must have an element at index where <X>
	- Must contain a list where each element <X>
- Run Time (Lintable?)
	
	
#### Must Extend Construct
If element is available, test it for extending the given construct.
Otherwise, check if the requirements for one are available.
If the requirements match or are tighter, then pass on.