# DICE-Fault-Injection-Tool
DICE Fault Injection tool, used to generate faults within Virtual Machines and a FCO Cloud Provider.
To access the VM level and issue commands the DICE tools use JSCH (http://www.jcraft.com/jsch/) to SSH to the Virtual Machines.
When using any argument that requires VM password or SSHKeypath use "-no" for argument not used. 
Example command: --stressmem 2 512m ubuntu@111.222.333.444 -no c://SSHKEYS/VMkey.key


Current User/VM level faults:
* Shutdown random VM (Ignore tagged VM with "noshutdown" in random selection)
* High CPU for VM (Using Stress tool)
* High Memeory usage for VM (Using Memtest tool)
* Block VM external access (Using ufw)
* Stop service running on VM
* Shutdown random VM from whitelist provided by user (Note the whitelist does not check if VM  exists or is a in a running state)

### CommandLineParameters:
usage: CommandLineParameters

    $ -f,--file <arg> Load from properties file.
    > -h,--help Shows help.
    > -m,--stressmem <memorytesterloops,memeorytotal,host,vmpassword,sshkeypath> Stress VM Memory.
    > -r,--randomVM <cloudusername, cloudpassword, cloudUUID,cloudapiurl> Shutdown random VM within FCO.
    > -s,--stresscpu <cores, stresstime, host, vmpassword, sshkeypath> Stress VM CPU.
    > -b,--blockfirewall <host,vmpassword,sshkeypath> Block external communication from Firewall.
    > -k,--killservice <host,vmpassword,service,sshkeypath> Stop service running on VM.
    > -w,--whitelist <cloudusername, cloudpassword, vmpassword, filepath> Shutdown random VM within FCO from testfile list
