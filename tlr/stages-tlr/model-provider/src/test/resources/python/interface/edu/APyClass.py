class AClass:
    # This is a Comment for AClass
    class_variable = "Class Variable for AClass" # This is an inline comment for class_variable of AClass

    def __init__(self, name):
        self.name = name
        self.instance_variable = "Instance Variable for AClass"

    def display_name(self):
        print(f"AClass name: {self.name}")
        print(f"AClass instance variable: {self.instance_variable}")
        print(f"AClass class variable: {AClass.class_variable}")

    class InnerClass1:
        """
        This is a multiple line 
        comment for InnerClass1
        """
        class_variable = "Class Variable for InnerClass1"

        def __init__(self, value):
            self.value = value
            self.instance_variable = "Instance Variable for InnerClass1"

        def display_value(self):
            print(f"InnerClass1 value: {self.value}")
            print(f"InnerClass1 instance variable: {self.instance_variable}")
            print(f"InnerClass1 class variable: {AClass.InnerClass1.class_variable}")

    class InnerClass2:
        '''
        This is a multiple line comment
        for InnerClass2
        '''
        class_variable = "Class Variable for InnerClass2"

        def __init__(self, value):
            self.value = value
            self.instance_variable = "Instance Variable for InnerClass2"

        def display_value(self):
            print(f"InnerClass2 value: {self.value}")
            print(f"InnerClass2 instance variable: {self.instance_variable}")
            print(f"InnerClass2 class variable: {AClass.InnerClass2.class_variable}")

class BClass:
    class_variable = "Class Variable for BClass"
