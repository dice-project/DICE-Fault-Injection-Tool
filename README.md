# DICE-Fault-Injection-Tool
DICE Fault Injection tool, used to generate faults within Virtual Machines and a FCO Cloud Provider.
To access the VM level and issue commands the DICE tools use JSCH (http://www.jcraft.com/jsch/) to SSH to the Virtual Machines.
When using any argument that requires VM password or SSHKeypath use "-no" for argument not used. 
Example command: --stressmem 2 512m ubuntu@111.222.333.444 -no c://SSHKEYS/VMkey.key

Supported OS:
* Ubuntu - Tested Ubuntu 14.0, 15.10
* Centos (With set Repo configured & wget installed) - Tested Centos 7


Current User/VM level faults:
* Shutdown random VM (Ignore tagged VM with "noshutdown" in random selection)
* High CPU for VM (Using Stress tool)
* High Memory usage for VM (Using Memtest tool)
* Block VM external access (Using ufw)
* High Bandwidth usage. (Using iperf, requires extrnal iperf server ip to be passed)
* High Disk I/O usage (Using Bonnie ++)
* Stop service running on VM
* Shutdown random VM from whitelist provided by user (Note the whitelist does not check if VM  exists or is a in a running state)
* Call YCSB on VM running MongoDB to begin workload test.
* Run JMeter plan

### CommandLineParameters:
usage: CommandLineParameters

    $ -f,--file <arg> Load from properties file.
    > -h,--help Shows help.
    > -m,--stressmem <memorytesterloops,memeorytotal,host,vmpassword,sshkeypath> Stress VM Memory.
    > -r,--randomVM <cloudusername, cloudpassword, cloudUUID,cloudapiurl> Shutdown random VM within FCO.
    > -s,--stresscpu <cores,time, host, vmpassword, sshkeypath> Stress VM CPU.
    > -b,--blockfirewall <host,vmpassword,sshkeypath> Block external communication from Firewall.
    > -k,--killservice <host,vmpassword,service,sshkeypath> Stop service running on VM.
    > -w,--whitelist <cloudusername, cloudpassword, vmpassword, filepath> Shutdown random VM within FCO from testfile list
    > -n,--stressnetwork <host, vmpassword, iperfserver, time, sshkeypath> High bandwitdh usage of VM 
    > -d,--diskstress <host, vmpassword, n,memeorytotal, loops, sshkeypath> High Disk usage of VM 
    > -y,--ycsb <host, vmpassword, workloadname, threads, sshkeypath> Start ycsb workload on MongoDB db
    > -j,--jmeter <hostm vmpassword,workloadname,planname,sshkeypath> Start pre-made Jmeter path


