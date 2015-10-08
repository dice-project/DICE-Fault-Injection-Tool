# DICE-Fault-Injection-Tool
DICE Fault Injection tool, used to generate faults within Virtual Machines and a FCO Cloud Provider.
To access the VM level and issue commands the DICE tools use JSCH (http://www.jcraft.com/jsch/) to SSH to the Virtual Machines.



Current User/VM level faults:
* Shutdown random VM (Ignore tagged VM with "noshutdown" in random selection)
* High CPU for VM (Using Stress tool)
* High Memeory usage for VM (Using Memtest tool)
* Block VM external access (Using ufw)
* Stop service running on VM
* Shutdown random VM from whitelist provided by user

### CommandLineParameters:

    $ -f,--file <arg> Load from properties file.
    > -h,--help Shows help.
    > -m,--stressmem <host,vmpassword,memorytesterloops,memeorytotal> Stress VM Memory.
    > -r,--randomVM <cloudusername, cloudpassword, vmpassword> Shutdown random VM within FCO.
    > -s,--stresscpu <cores, stresstime, vmpassword, host> Stress VM CPU.
    > -b,--blockfirewall <host, vmpassword> Block external communication from Firewall.
    > -k,--killservice <host, vmpassword, service> Stop service running on VM.
    > -w,--whitelist <cloudusername, cloudpassword, vmpassword, filepath> Shutdown random VM within FCO from testfile list
