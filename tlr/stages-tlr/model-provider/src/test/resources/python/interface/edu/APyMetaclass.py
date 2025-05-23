class APyMetaclass(type):
    def __new__(cls, name, bases, dct):
        print(f"Creating class {name} with bases {bases} and attributes {dct}")
        return super().__new__(cls, name, bases, dct)

    def __init__(cls, name, bases, dct):
        print(f"Initializing class {name}")
        super().__init__(name, bases, dct)
