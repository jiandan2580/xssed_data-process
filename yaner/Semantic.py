#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''链接：https://cloud.tencent.com/developer/article/1411370    python实现的表达式树'''
class StackEmptyException(Exception): pass


class StackFullException(Exception): pass


class Node:
    def __init__(self, val=None, nxt=None):
        self.value = val
        self.next = nxt

    def __str__(self):
        return str(self.value)


class Stack:

    def __init__(self, max=0):
        self._top = None
        self._max = 0
        self.max = max

    @property
    def max(self):
        return self._max

    @max.setter
    def max(self, m):
        m = int(m)
        if m < self.length:
            raise Exception('Resize stack failed, please pop some elements first.')
        self._max = m
        if self._max < 0:
            self._max = 0

    def init(self, iterable=()):
        if not iterable:
            return
        self._top = Node(iterable[0])
        for i in iterable[1:]:
            node = self._top
            self._top = Node(i)
            self._top.next = node

    def show(self):
        def _traversal(self):
            node = self._top
            while node and node.next:
                yield node
                node = node.next
            yield node

        print('\n'.join(map(lambda x: '|{:^7}|'.format(str(x)), _traversal(self))) + '\n ' + 7 * '-')

    @property
    def length(self):
        if self._top is None:
            return 0
        node = self._top
        i = 1
        while node.next:
            node = node.next
            i += 1
        return i

    @property
    def is_empty(self):
        return self._top is None

    @property
    def is_full(self):
        return bool(self._max and self.length == self._max)

    def push(self, item):
        if self.is_full:
            raise StackFullException('Error: trying to push element into a full stack!')
        if not self._top:
            self._top = Node(item)
            return
        node = self._top
        self._top = Node(item)
        self._top.next = node

    def pop(self):
        if self.is_empty:
            raise StackEmptyException('Error: trying to pop element from an empty stack!')
        node = self._top
        self._top = self._top.next
        return node.value

    def top(self):
        return self._top.value if self._top else self._top

    def clear(self):
        while self._top:
            self.pop()


def test(stack):
    print('\nShow stack:')
    stack.show()

    print('\nInit linked list:')
    stack.init([1, 2, 3, 4, 5])
    stack.show()

    print('\nPush element to stack:')
    stack.push(6)
    stack.push(7)
    stack.push('like')
    stack.show()

    print('\nCheck top element:')
    print(stack.top())

    print('\nPop element from stack:')
    e = stack.pop()
    print('Element %s popped,' % e)
    stack.show()

    print('\nSet stack max size:')
    try:
        stack.max = 1
    except Exception as e:
        print(e)

    print('\nSet stack max size:')
    stack.max = 7
    print(stack.max)

    print('\nPush full stack:')
    try:
        stack.push(7)
    except StackFullException as e:
        print(e)

    print('\nClear stack:')
    stack.clear()
    stack.show()

    print('\nStack is empty:')
    print(stack.is_empty)

    print('\nPop empty stack:')
    try:
        stack.pop()
    except StackEmptyException as e:
        print(e)


class TreeNode:
    def __init__(self, val=None, lef=None, rgt=None):
        self.value = val
        self.left = lef
        self.right = rgt

    def __str__(self):
        return str(self.value)


class BinaryTree:
    def __init__(self, root=None):
        self._root = root

    def __str__(self):
        return '\n'.join(map(lambda x: x[1]*4*' '+str(x[0]), self.pre_traversal()))

    def pre_traversal(self, root=None):
        if not root:
            root = self._root
        x = []
        depth = -1

        def _traversal(node):
            nonlocal depth
            depth += 1
            x.append((node, depth))
            if node and node.left is not None:
                _traversal(node.left)
            if node and node.right is not None:
                _traversal(node.right)
            depth -= 1
            return x
        return _traversal(root)

    def in_traversal(self, root=None):
        if not root:
            root = self._root
        x = []
        depth = -1

        def _traversal(node):
            nonlocal depth
            depth += 1
            if node and node.left is not None:
                _traversal(node.left)
            x.append((node, depth))
            if node and node.right is not None:
                _traversal(node.right)
            depth -= 1
            return x
        return _traversal(root)

    def post_traversal(self, root=None):
        if not root:
            root = self._root
        x = []
        depth = -1

        def _traversal(node):
            nonlocal depth
            depth += 1
            if node and node.left is not None:
                _traversal(node.left)
            if node and node.right is not None:
                _traversal(node.right)
            x.append((node, depth))
            depth -= 1
            return x
        return _traversal(root)

    @property
    def max_depth(self):
        return sorted(self.pre_traversal(), key=lambda x: x[1])[-1][1]

    def show(self, tl=None):
        if not tl:
            tl = self.pre_traversal()
        print('\n'.join(map(lambda x: x[1]*4*' '+str(x[0]), tl)))

    def make_empty(self):
        self.__init__()

    def insert(self, item):
        if self._root is None:
            self._root = TreeNode(item)
            return

        def _insert(item, node):
            if not node:
                return TreeNode(item)
            if node.left is None:
                node.left = _insert(item, node.left)
            elif node.right is None:
                node.right = _insert(item, node.right)
            else:
                if len(self.pre_traversal(node.left)) <= len(self.pre_traversal(node.right)):
                    node.left = _insert(item, node.left)
                else:
                    node.right = _insert(item, node.right)
            return node
        self._root = _insert(item, self._root)


class ExpressionTree(BinaryTree):
    SIGN = {'+': 1, '-': 1, '*': 2, '/': 2, '(': 3}

    def gene_tree_by_postfix(self, expr):
        s =Stack()
        for i in expr:
            if i in self.SIGN.keys():
                right = s.pop()
                left = s.pop()
                node = TreeNode(i, left, right)
                s.push(node)
            else:
                s.push(TreeNode(i))
        self._root = s.pop()

class ExpressionTree(BinaryTree):
    SIGN = {'+': 1, '-': 1, '*': 2, '/': 2, '(': 3}

    def gene_tree_by_postfix(self, expr):
        s = Stack()
        for i in expr:
            if i in self.SIGN.keys():
                right = s.pop()
                left = s.pop()
                node = TreeNode(i, left, right)
                s.push(node)
            else:
                s.push(TreeNode(i))
        self._root = s.pop()


def test_expression_tree(ep):
    t = ExpressionTree()
    t.gene_tree_by_postfix(ep)
    print('\n------先序遍历-------')
    print(t)
    print('\n------后序遍历------')
    t.show(t.post_traversal())
    print('\n-------中序遍历-------')
    t.show(t.in_traversal())

if __name__ == '__main__':
    ep = 'a b + c d e + * *'
    test_expression_tree(ep.split(' '))