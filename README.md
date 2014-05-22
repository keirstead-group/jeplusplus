# jE++

jE++ is a streamlined Java interface to [jEPlus](http://www.jeplus.org/).  Whereas jEPlus provides users with a detailed GUI to set up and run [EnergyPlus](http://apps1.eere.energy.gov/buildings/energyplus/) models, jE++ only lets you run a pre-configured project from a specified working directory.  

jE++ was written by James Keirstead at Imperial College London.  It is released under the GPL3 license.

## Motivation

[EnergyPlus](http://apps1.eere.energy.gov/buildings/energyplus/) is a software package for building energy simulation.  Users specify models by writing specially-formatted text files (with `*.idf` or `*.imf` extensions by convention)  to describe the building's design, mechanical and electrical systems, occupancy patterns, and so on.  The results of such simulations can include energy consumption statistics at various temporal resolutions, as well as metrics of building comfort.

[jEPlus](http://www.jeplus.org/) is a tool for parameteric simulation of EnergyPlus models, developed by Yi Zhang and Ivan Korolija at De Montfort University.  It allows users to replace model parameters with special tags and then run Monte Carlo simulations to explore how a model performs under uncertain inputs.  For example, one might replace the setpoint of an EnergyPlus thermostat and then simulate the buildings performance over 100 evenly spaced intervals between 15 and 25 degrees Celsius.  However jEPlus is designed to be run via its user interface and, although the [source code is available](http://sourceforge.net/p/jeplus/code/HEAD/tree/), the documentation is sparse, making it difficult to interface with jEPlus programmatically.  

jE++ is therefore designed to interface with jEPlus's command line interface, using the precompiled jar file.  Basic control of model runs is available and for many situations this will be sufficient.

## System requirements

To use jE++, you need the following software installed:

 * [EnergyPlus](http://apps1.eere.energy.gov/buildings/energyplus/energyplus_about.cfm)  This can be downloaded free of charge (with registration).  The current version is 8.1.0, although I had to revert to [8.0](http://apps1.eere.energy.gov/buildings/energyplus/energyPlus_download.cfm?previous) to get the demo included with jE++ to work.  _Note that EnergyPlus must be installed on a path without spaces_.
 
 * [jEPlus](http://www.jeplus.org/wiki/doku.php?id=download:start) is released under the GPL3 license and can be freely downloaded.  I'm currently using version `v1.5_pre_05`.  You will require Java to run jEPlus (and jE++) as described on the linked download page.  When installing jEPlus, make a note of the installation folder as you will need to point jE++ to the jar file.
 
## How to use

Here's a simple example of how to use jE++.

1. Start a new Java project in Eclipse (or whatever IDE you prefer).  Add the jE++ project to the build path.

1. In your Java code, set the path to the jEPlus jar:

   ```java
   JEPlusController.setJarPath("/path/to/jEPlus.jar");
   ```

1. Create a folder (say `~/jepp_test`) containing the following files. Configuring these files can be a little tricky; please see the `demo` directory for an example.
  * An EnergyPlus model file ending in `.imf`
  * An EnergyPlus weather file ending in `.epw`
  * An EnergyPlus output file ending in `.mvi`
  * A jEPlus project file ending in `.jep`
   
1. Define a new JEPlusProject object with the following code.  You can of course change the path objects to point wherever you like.

   ```java
   Path indir = Paths.get("~/jepp_test");
   Path outdir = indir.resolve("output");
   JEPlusProject project = new JEPlusProject(indir, outdir);
   ```

5. By default jE++ will add all of the required files listed in Step 2 to the project.  If you have extra files to include, for example if you use a `Schedule:File` object in your `*.imf` file, then these can be added with:

   ```java
   File file = new File("my-extra-file.txt");
   project.addFiles(file);
   ```

6. You can also use jE++ to set fixed jEPlus parameters.  That is, rather than using jEPlus's sampling architecture to perform multiple runs for an uncertain parameter, jE++ allows you to fix these to a single value.  This is particularly helpful if you want to, for example, run a model for a different month of the year.  In this case, assuming you had a `@@month` parameter defined in the your `.imf` and `.jep` files, ranging from 1 to 12, and you wanted to fix it to the 7th value (i.e. July), then you could type:

   ```java
   project.setFixedParameterValue("@@month@@", 7);
   ```

7. When you're ready, the model can be run as follows.  

   ```java
   project.run();
   ```

   The results will be placed in the output directory specified in the constructor.

8. If you would like to scale the results by an arbitrary factor, this can be done with the following code.  Note that this will transform all of the columns in the simulation results file except the first three (which are time steps and scenario information).  Obviously this only makes sense if the outputs are things like energy consumption, rather than say temperature.

   ```java
   project.scaleResults(2.5);
   ```



