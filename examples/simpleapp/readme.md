**Simple App**
---------

Simple app is a Java application that allows to run a pure Sidechain Node without any custom logic like custom boxes and transactions. Forward and Backward transfers are available out of the box.

It is possible to run a SC node or nodes with or without real MC node connection.

**Execution**

You can run Simple App inside IDE just executing SimpleApp class in examples/simpleapp module.

Or if you want to run SimpleApp outside, than:
1. Build and package the project
    ```
    mvn package
    ```
2. Go to project root dir and execute in a command line:
    * For Windows:
        ```
        java -jar ./examples/simpleapp/target/Sidechains-SDK-simpleapp-0.1-SNAPSHOT.jar <path_to_config_file>
        ```
    * For Linux:
        ```
        java -jar ./examples/simpleapp/target/Sidechains-SDK-simpleapp-0.1-SNAPSHOT.jar <path_to_config_file>
        ```
    
**Running SimpleApp isolated from MC**

To run application you can use predefined configuration file placed into `./examples/simpleapp/src/main/resourse/sc_settings.conf`

This config already contains some genesis data that will start a node with a first MC Block Reference and some coins transferred to current SC node.

Why it is "isolated"? Because inside SC genesis block declared a MC Block Reference that has no way to continue the mainchain. So all next SC blocks will not have any new MC data.

Such a configuration is useful when you want just to test Sidechain logic and behaviour without any interaction with a Mainchain network.



**Running SimpleApp connected to the real MC node**

That mode allow to use full Sidechain node functionality, i.e. create new Sidechains, synchronize Sidechain to Mainchain (Mainchain block references are included in Sidechain blocks), transfer coins from/to Mainchain and vice versa.
Please read detailed [guide](mc_sc_workflow_example.md) how to setup that mode.

**How to choose the address of the first forward transfers during creation?**

You specified in `sc_create` RPC method two public keys: from Vrf keypair and from ed25519 keypair.
To generate a new pair of secret-public keys you can use ScBootstrappingTool command `generatekey` and `generateVrfKey` respectively.
For example `generatekey {"seed":"myuniqueseed"}` and `generateVrfKey {"seed":"my seed"}`

Then you can put public parts as a destination of `sc_create` and secret into configuration file in a section `wallet.genesisSecrets`. 
Note: if you will not specify `genesisSecrets` properly, you will not see that balances in the wallet. 


**How to choose the secret in ScBootstrapping `genesisinfo` command?**
Just use any secret you want generated by `generatekey` command.

**SC related RPC commands in a MC**
1. `sc_create "scid" withdrawalEpochLength "address" amount "verification key" "vrfPublickKey" "genSysConstant"` - to create a new SC, please see [example](mc_sc_workflow_example.md) for more details
2. `sc_send "scid"  "address" amount "sidechainId"` - to send single forward transfer to desired address in a proper sidechain.
3. `sc_sendmany [{"address":... ,"amount":...,"scid":,...},...]` - to send multiple transfers to different addresses/sidechains

For more info look into the MC repository and guides.

**Sidechain HTTP API**

Node API address specified in configuration file in a section `network.bindAddress`.

Description of all basic API with example is placed in a simple web interface, to reach it just type `network.bindAddress` in any browser.

