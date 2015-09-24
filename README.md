# DICE-Fault-Injection-Tool
DICE Fault Injection tool, used to generate faults within Virtual Machines and Cloud Provider.

Current User/VM level faults:
* Shutdown random VM (Ignore tagged VM with "noshutdown" in random selection)
* High CPU for VM
* High Memeory usage for VM

### CommandLineParameters:

    $ -f,--file <arg> Load from properties file.
    > -h,--help Shows help.
    > -m,--stressmem <host,vmpassword,memorytesterloops,memeorytotal> Stress VM Memory.
    * -r,--randomVM <cloudusername, cloudpassword, vmpassword, host> Shutdown random VM within FCO.
    * -s,--stresscpu <cores, stresstime, vmpassword, host> Stress VM CPU.
